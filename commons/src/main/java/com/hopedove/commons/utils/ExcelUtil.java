package com.hopedove.commons.utils;

import com.hopedove.commons.exception.BusinException;
import com.hopedove.commons.exception.ErrorCode;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import sun.awt.geom.AreaOp;

import javax.print.attribute.standard.PrinterLocation;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class ExcelUtil {
    private final static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    /**
     * Do export.
     *文件导出成文件流
     * @param response the response
     * @param dataList the data list<Map> 需要导出的数据列表
     * @param execlName the execl name 导出后默认的文件名
     * @param tmpContent the tmp content 数据库字段名，多字段以逗号分割
     * @param tmpContentCn the tmp content cn 文件字段名，多字段以逗号分割
     *
     * @throws Exception the exception
     */
    public  static void  doExport(HttpServletResponse response, List<?> dataList, String execlName, String tmpContent, String tmpContentCn) throws Exception{
        //生成excel
        HSSFWorkbook workbook = printExcel(tmpContent,tmpContentCn,dataList);
        //导出excel
        writeExecl(response,workbook,execlName);
    }

    /**
     * Prints the excel.
     *
     * @param tmpContent the tmp content
     * @param tmpContentCn the tmp content cn
     * @param dataList the data list
     *
     * @return the hSSF workbook
     */
    private  static HSSFWorkbook printExcel(String tmpContent,String tmpContentCn,List dataList){

        HSSFWorkbook workbook = null;
        String[] titles_CN = tmpContentCn.split(",");
        String[] titles_EN = tmpContent.split(",");
        try{
            //创建工作簿实例
            workbook = new HSSFWorkbook();
            //创建工作表实例
            HSSFSheet sheet = workbook.createSheet("Sheet1");
            //设置列宽
            setSheetColumnWidth(titles_CN,sheet);
            //获取样式
            HSSFCellStyle style = createTitleStyle(workbook);
            if(dataList != null){
                //创建第一行标题
                HSSFRow row = sheet.createRow((short)0);// 建立新行
                for(int i=0;i<titles_CN.length;i++){
                    createCell(row, i, null, HSSFCell.CELL_TYPE_STRING,
                            titles_CN[i]);
                }
                //给excel填充数据
                for(int i=0;i<dataList.size();i++){
                	
                    // 将dataList里面的数据取出来
                   // Map<String,String> map = (HashMap)(dataList.get(i));
                //2020-03-25 wsm 实体类转map
                	Object object = dataList.get(i);
                    Map<String, Object> map = new HashMap();
                    for (Field field : object.getClass().getDeclaredFields()){
                      try {
                        boolean flag = field.isAccessible();
                        field.setAccessible(true);
                        Object o = field.get(object);
                        map.put(field.getName(), o);
                        field.setAccessible(flag);
                      } catch (Exception e) {
                        e.printStackTrace();
                      }
                    }
                    HSSFRow row1 = sheet.createRow((short) (i + 1));// 建立新行

                    boolean isOverTime = false;
                    for(int j=0;j<titles_EN.length;j++){
                        createCell(row1, j, style, HSSFCell.CELL_TYPE_STRING, map.get(titles_EN[j]));
                    }
                }
            }else{
                createCell(sheet.createRow(0), 0, style,HSSFCell.CELL_TYPE_STRING, "查无资料");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return workbook;
    }

    //创建Excel单元格
    /**
     * Creates the cell.
     *
     * @param row the row
     * @param column the column
     * @param style the style
     * @param cellType the cell type
     * @param value the value
     */
    private static void createCell(HSSFRow row, int column, HSSFCellStyle style,int cellType,Object value) {
        HSSFCell cell = row.createCell( column);
        if (style != null) {
            cell.setCellStyle(style);
        }
        String res = (value==null?"":value).toString();
        switch(cellType){
            case HSSFCell.CELL_TYPE_BLANK: {} break;
            case HSSFCell.CELL_TYPE_STRING: {cell.setCellValue(res+"");} break;
            case HSSFCell.CELL_TYPE_NUMERIC: {
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(Double.parseDouble(value.toString()));}break;
            default: break;
        }

    }

    //设置列宽
    /**
     * Sets the sheet column width.
     *
     * @param titles_CN the titles_ cn
     * @param sheet the sheet
     */
    private static void setSheetColumnWidth(String[] titles_CN,HSSFSheet sheet){
        // 根据你数据里面的记录有多少列，就设置多少列
        for(int i=0;i<titles_CN.length;i++){
            sheet.setColumnWidth((short)i, 3000);
        }

    }

    //设置excel的title样式
    /**
     * Creates the title style.
     *
     * @param wb the wb
     *
     * @return the hSSF cell style
     */
    private static HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
        HSSFFont boldFont = wb.createFont();
        boldFont.setFontHeight((short) 200);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(boldFont);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,##0.00"));
        //style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //style.setFillBackgroundColor(HSSFColor.LIGHT_ORANGE.index);
        return style;
    }

    /**
     * Write execl.
     *
     * @param response the response
     * @param workbook the workbook
     * @param execlName the execl name
     */
    public static void writeExecl(HttpServletResponse response,HSSFWorkbook workbook, String execlName) {
        if (null == workbook)
        {
            workbook = new HSSFWorkbook();
        }

        if (0 == workbook.getNumberOfSheets()) {
            HSSFSheet sheet = workbook.createSheet("无数据");
            sheet.createRow(3).createCell(3).setCellValue("未查询到数据!");
        }
        response.reset();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            //new String(execlName.getBytes("gb2312"), "ISO8859-1")
            response.setHeader("Content-disposition", "attachment; filename="
                    +URLEncoder.encode(execlName,"utf-8")
                    + "_" + LocalDateTimeUtil.formatTime(LocalDateTime.now(), "MM-dd") + ".xls");
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }
    }

    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        InputStream is = null;
        try {
            //获取excel文件的io流
            is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if(fileName.endsWith(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(fileName.endsWith(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }finally {
            if(null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return workbook;
    }

    public static Workbook getWorkBook(InputStream is,String extName) {
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            if(extName.equals(xls)){
                //2003
                workbook = new HSSFWorkbook(is);
            }else if(extName.equals(xlsx)){
                //2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }finally {
            if(null!=is){
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return workbook;
    }

    public static String getCellValue(Cell cell){
        if (cell == null)
            return "";
        String result = "";
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_FORMULA:
                result = cell.getCellFormula();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    // SimpleDateFormat datefomrat=new SimpleDateFormat("yyyy-MM-dd
                    // HH:mm:ss");
                    SimpleDateFormat datefomrat = new SimpleDateFormat("yyyy/MM/dd");
                    if (date == null)
                        result = "";
                    else
                        result = datefomrat.format(cell.getDateCellValue());
                } else {
                    //result = String.valueOf(cell.getNumericCellValue());
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    result = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                result = String.valueOf(cell.getRichStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                result = "";
                break;
            default:
                result = "";
        }
        return result;
    }

    public static void checkFile(MultipartFile file) throws BusinException{
        //判断文件是否存在
        if(null == file){
            log.error("文件不存在！");
            throw new BusinException(ErrorCode.EXP_FILE_NOTEXISTS);
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){
            log.error(fileName + "不是excel文件");
            throw new BusinException(ErrorCode.EXP_FILE_NOTEXISTS);
        }
    }

    /**
     * 验证表头
     * @param sheet
     * @param head
     */
    public static void checkHead(Sheet sheet,String[] head){
        Row rowHead = sheet.getRow(0);
        for (int i = 0; i < head.length; i++) {
            Cell cell = rowHead.getCell(i);
            String title = head[i];
            if(!title.equals(getCellValue(cell))){
                log.error("excel列头不符合规范");
                throw new BusinException(ErrorCode.EXP_EXCEL_HEAD);//列头不正确
            }
        }
    }

    /**
     * 读入excel文件，解析后返回
     * @param file
     * @parm head 列头数组可以为null
     * @param sheetInt 第几页
     * @throws IOException
     */
    public static List<String[]> readExcel(MultipartFile file,String[] head,Integer sheetInt) throws IOException{
        //检查文件
        checkFile(file);

        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        return getExcelList(workbook,head,sheetInt);
    }

    /**
     * 读入excel文件，解析后返回
     * @param bytes
     * @param  extName 扩展名
     * @parm head 列头数组可以为null
     * @param sheetInt 第几页
     * @throws IOException
     */
    public static List<String[]> readExcel(byte[] bytes,String extName,String[] head,Integer sheetInt) throws IOException{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        //判断文件是否是excel文件
        if(!extName.equals(xls) && !extName.equals(xlsx)){
            log.error("不是excel文件");
            throw new BusinException("600","不是excel文件");
        }
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(bis,extName);

        return getExcelList(workbook,head,sheetInt);
    }

    public static List<String[]> getExcelList(Workbook workbook,String[] head,Integer sheetInt){
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<String[]>();
        if(workbook != null){
            if(sheetInt==null){
                sheetInt = 1;
            }else{
                sheetInt = workbook.getNumberOfSheets();
            }
            for(int sheetNum = 0;sheetNum < sheetInt;sheetNum++){
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if(sheet == null){
                    continue;
                }

                if(null!=head&&head.length>0){
                    checkHead(sheet,head);//检查EXCEL列头
                }
                //获得当前sheet的开始行
                int firstRowNum  = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                if(lastRowNum==0){
                    throw new BusinException("600","该EXCEL是空文件");
                }
                //循环除了第一行的所有行
                for(int rowNum = firstRowNum+1;rowNum <= lastRowNum;rowNum++){
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if(row == null){
                        continue;
                    }
                    //获得当前行的开始列
                    int firstCellNum = 0;
                    //获得当前行的列数
                    int lastCellNum = head.length;
                    String[] cells = new String[head.length];
                    //循环当前行
                    for(int cellNum = firstCellNum; cellNum < lastCellNum;cellNum++){
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                }
            }
        }
        return list;
    }
}
