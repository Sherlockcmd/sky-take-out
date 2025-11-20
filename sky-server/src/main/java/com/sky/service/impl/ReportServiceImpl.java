package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.entity.User;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        List<Double> sumList = new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        for(LocalDate date : dateList){
            //查询date日期对应的营业额数据，营业额是状态为”已完成“的订单金额合计
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //select sum(amount) from orders where order_time > beginTime and order_time < endTime and status = 5
            Integer status = Orders.COMPLETED;
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status",status);

            Double ordersum = orderMapper.sumByMap(map);
            if(ordersum==null){
                ordersum=0.0;
            }
            sumList.add(ordersum);
        }


        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .turnoverList(StringUtils.join(sumList,","))
                .build();
    }

    @Override
    public UserReportVO getuserStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        //存放当前某天的用户总量
        List<Integer> usersum = new ArrayList<>();
        //存放当前某天的新增用户总量
        List<Integer> useraddsum= new ArrayList<>();

        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end",endTime);
            Integer sumUser = userMapper.sumUserByMap(map);

            map.put("begin",beginTime);
            Integer sumUseradd = userMapper.sumUserByMap(map);

            sumUser = sumUser == null ? 0 :sumUser;
            sumUseradd = sumUseradd == null ? 0 :sumUseradd;

            usersum.add(sumUser);
            useraddsum.add(sumUseradd);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(usersum,","))
                .newUserList(StringUtils.join(useraddsum,","))
                .build();
    }

    @Override
    public OrderReportVO getordersStatistics(LocalDate begin, LocalDate end) {
        //当前集合用于存放从begin到end范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        //订单总数
        Integer totalOrderCount=0;
        //有效订单总数
        Integer validtotalOrderCount=0;


        dateList.add(begin);

        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        for(LocalDate date : dateList){
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);

            Integer orderCount = orderMapper.countByMap(map);

            map.put("status",Orders.COMPLETED);

            Integer validOrderCount = orderMapper.countByMap(map);

            orderCount = orderCount==null ? 0 : orderCount;
            validOrderCount = validOrderCount==null ? 0 : validOrderCount;

            orderCountList.add(orderCount);
            totalOrderCount+=orderCount;
            validOrderCountList.add(validOrderCount);
            validtotalOrderCount+=validOrderCount;
        }

        //订单完成率
        Double orderCompletionRate=(1.0*validtotalOrderCount/totalOrderCount)*0.01;

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validtotalOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {

        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();

        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);

        List<GoodsSalesDTO> salesTop = orderMapper.getSalesTOP(beginTime,endTime);

        for(GoodsSalesDTO goodsSalesDTO : salesTop){
            nameList.add(goodsSalesDTO.getName());
            numberList.add(goodsSalesDTO.getNumber());
        }
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList,","))
                .numberList(StringUtils.join(numberList,","))
                .build();
    }


    public void exportBusinessData(HttpServletResponse response) {
        //1.查询数据库，获取营业数据--查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30); //减30天的时间
        LocalDate dateEnd = LocalDate.now().minusDays(1);
        BusinessDataVO businessDatavo = workspaceService.businessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));
        //2.通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");//在类路径下读取资源返回输入流对象

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);
            //获取表格文件的Sheet文件
            XSSFSheet sheet = excel.getSheet("Sheet1");
            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDatavo.getTurnover()); //第3个单元格
            row.getCell(4).setCellValue(businessDatavo.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDatavo.getNewUsers());
            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDatavo.getValidOrderCount());
            row.getCell(4).setCellValue(businessDatavo.getUnitPrice());
            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                workspaceService.businessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessDatavo.getTurnover());
                row.getCell(3).setCellValue(businessDatavo.getValidOrderCount());
                row.getCell(4).setCellValue(businessDatavo.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDatavo.getUnitPrice());
                row.getCell(6).setCellValue(businessDatavo.getNewUsers());
            }
            //3.通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);
            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
