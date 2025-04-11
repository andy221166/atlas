package org.atlas.infrastructure.file.excel.poi.adapter.product;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.atlas.framework.constant.CommonConstant;
import org.atlas.framework.file.excel.ProductExcelWriterPort;
import org.atlas.framework.file.model.write.ProductRow;
import org.atlas.framework.util.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class ProductExcelWriterAdapter implements ProductExcelWriterPort {

  @Override
  public byte[] write(List<ProductRow> productRows) throws Exception {
    try (XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      XSSFSheet sheet = workbook.createSheet(SHEET_NAME);
      createHeader(workbook, sheet);
      createRows(workbook, sheet, productRows);
      sheet.autoSizeColumn(1);
      workbook.write(outputStream);
      return outputStream.toByteArray();
    }
  }

  private void createHeader(XSSFWorkbook workbook, XSSFSheet sheet) {
    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();
    XSSFFont font = workbook.createFont();
    font.setFontName("Calibri");
    font.setFontHeightInPoints((short) 12);
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("ID");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Name");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(2);
    headerCell.setCellValue("Price");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(3);
    headerCell.setCellValue("Image URL");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(4);
    headerCell.setCellValue("Quantity");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(5);
    headerCell.setCellValue("Status");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(6);
    headerCell.setCellValue("Available From");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(7);
    headerCell.setCellValue("Active");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(8);
    headerCell.setCellValue("Description");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(9);
    headerCell.setCellValue("Attribute Name 1");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(10);
    headerCell.setCellValue("Attribute Value 1");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(11);
    headerCell.setCellValue("Attribute Name 2");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(12);
    headerCell.setCellValue("Attribute Value 2");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(13);
    headerCell.setCellValue("Attribute Name 3");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(14);
    headerCell.setCellValue("Attribute Value 3");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(15);
    headerCell.setCellValue("Brand ID");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(16);
    headerCell.setCellValue("Category IDs");
    headerCell.setCellStyle(headerStyle);
  }

  private void createRows(XSSFWorkbook workbook, XSSFSheet sheet, List<ProductRow> productRows) {
    int rowIndex = 1;
    for (ProductRow productRow : productRows) {
      CellStyle style = workbook.createCellStyle();
      style.setAlignment(HorizontalAlignment.LEFT);
      style.setWrapText(true);

      Row row = sheet.createRow(rowIndex);
      Cell cell = row.createCell(0);
      cell.setCellValue(productRow.getId());
      cell.setCellStyle(style);

      cell = row.createCell(1);
      cell.setCellValue(productRow.getName());
      cell.setCellStyle(style);

      cell = row.createCell(2);
      cell.setCellValue(productRow.getPrice().doubleValue());
      cell.setCellStyle(style);

      cell = row.createCell(3);
      cell.setCellValue(productRow.getImageUrl());
      cell.setCellStyle(style);

      cell = row.createCell(4);
      cell.setCellValue(productRow.getQuantity());
      cell.setCellStyle(style);

      cell = row.createCell(5);
      cell.setCellValue(productRow.getStatus().name());
      cell.setCellStyle(style);

      cell = row.createCell(6);
      cell.setCellValue(DateUtil.format(productRow.getAvailableFrom(), CommonConstant.DATE_TIME_FORMAT));
      cell.setCellStyle(style);

      cell = row.createCell(7);
      cell.setCellValue(productRow.getIsActive());
      cell.setCellStyle(style);

      cell = row.createCell(8);
      cell.setCellValue(productRow.getDescription());
      cell.setCellStyle(style);

      cell = row.createCell(9);
      cell.setCellValue(productRow.getAttributeName1());
      cell.setCellStyle(style);

      cell = row.createCell(10);
      cell.setCellValue(productRow.getAttributeValue1());
      cell.setCellStyle(style);

      cell = row.createCell(11);
      cell.setCellValue(productRow.getAttributeName2());
      cell.setCellStyle(style);

      cell = row.createCell(12);
      cell.setCellValue(productRow.getAttributeValue2());
      cell.setCellStyle(style);

      cell = row.createCell(13);
      cell.setCellValue(productRow.getAttributeName3());
      cell.setCellStyle(style);

      cell = row.createCell(14);
      cell.setCellValue(productRow.getAttributeValue3());
      cell.setCellStyle(style);

      cell = row.createCell(15);
      cell.setCellValue(productRow.getBrandId());
      cell.setCellStyle(style);

      cell = row.createCell(16);
      cell.setCellValue(productRow.getCategoryIds()
          .stream()
          .map(String::valueOf)
          .collect(Collectors.joining(",")));
      cell.setCellStyle(style);

      rowIndex++;
    }
  }
}
