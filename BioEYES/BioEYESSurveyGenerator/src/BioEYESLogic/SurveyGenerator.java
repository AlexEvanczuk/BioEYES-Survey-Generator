package BioEYESLogic;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class SurveyGenerator{
	//public static void main(String[] args){
	public static void createSurveyFiles(String filePath, String fileDestination)
	{
		try {
			FileInputStream fileInputStream = new FileInputStream(filePath);
			XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
			Sheet worksheet = workbook.getSheet("Blank");
			CreationHelper createHelper = workbook.getCreationHelper();
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
			workbook.setMissingCellPolicy(Row.CREATE_NULL_AS_BLANK);
			int rownum = 2;	
				boolean allcreated = false;
					while (allcreated == false){
						Row row = worksheet.getRow(rownum);	
						Cell status = row.getCell((short)12);
						Cell preCode = row.getCell((short)17);
						Cell postCode = row.getCell((short)18);
						Cell level = row.getCell((short)7);
						CellValue cellpreEvaluated = evaluator.evaluate(preCode);
						CellValue cellpostEvaluated = evaluator.evaluate(postCode);
						String cellpreValue = cellpreEvaluated.getStringValue();
						String cellpostValue = cellpostEvaluated.getStringValue();
						String levelValue = level.getRichStringCellValue().toString();;
					if(status.getCellType() == Cell.CELL_TYPE_BLANK) {
						if(cellpreValue.equals(",,,,,PRE,,") && cellpostValue.equals(",,,,,POST,,")){
							allcreated = true;		
							FileOutputStream fileOut = new FileOutputStream(filePath);
							workbook.write(fileOut);
							fileOut.close();
						} else {
							String sessionID = evaluator.evaluate(row.getCell((short)16)).getStringValue();					
							if(levelValue.equals("Advanced")){
								int xShift = 0;
								int yShift = 0;
								createPDF("Advanced Pre 2011.pdf", fileDestination, cellpreValue, sessionID, "PRE", xShift, yShift);
								createPDF("Advanced Post 2011.pdf", fileDestination, cellpostValue, sessionID, "POST", xShift, yShift);
							} else if(levelValue.equals("Intermediate")){
								int xShift = -5;
								int yShift = -37;
								createPDF("Intermediate Pre 2011.pdf", fileDestination, cellpreValue, sessionID, "PRE", xShift, yShift);
								createPDF("Intermediate Post 2011.pdf", fileDestination, cellpostValue, sessionID, "POST", xShift, yShift);
							} else if(levelValue.equals("Micro")){
								int xShift = 0;
								int yShift = -40;
								createPDF("Micro Pre 2011.pdf", fileDestination, cellpreValue, sessionID, "PRE", xShift, yShift);
								xShift = 0;
								yShift = -45;
								createPDF("Micro Post 2011.pdf", fileDestination, cellpostValue, sessionID, "POST", xShift, yShift);
							}
							System.out.println("status should be blank: " + status.getStringCellValue());
							row.createCell(12).setCellValue(createHelper.createRichTextString("Created Survey"));
							System.out.println("Level is: " + levelValue);
							rownum++;
						}
					}else{
						rownum++;
					}
				}
		} catch (FileNotFoundException e) {
			//System.out.print("Please close file");
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void createPDF(String surveyType, String fileDestination, String barCode, String sessionId, String preorPost, int xShift, int yShift){
		Document document = new Document();
		//String surveyType = "Advanced Post 2011.pdf";
		try{
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileDestination + "/" + sessionId + preorPost + ".pdf"));
		document.setPageSize(PageSize.LETTER); 
		document.setMargins(0,0,0,0);
		document.open();
		PDFCreator.importSurvey(document, writer, surveyType);
		PDFCreator.createPdf(document, writer, sessionId, barCode, xShift, yShift);
		}catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
		
}