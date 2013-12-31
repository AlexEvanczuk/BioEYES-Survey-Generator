package BioEYESLogic;

import java.io.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.*;

 
public class PDFCreator {
	public static void importSurvey(Document document, PdfWriter writer, String surveyType)
			throws IOException, DocumentException{
		PdfReader reader = new PdfReader(surveyType);
		for (int i = 1; i <= 2; i++) {
			PdfImportedPage page = writer.getImportedPage(reader, i);
			Image img = Image.getInstance(page);
			document.add(img);
		}	 
	}
	public static void createPdf(Document document, PdfWriter writer, String sessionID, String barCode, int xShift, int yShift) throws IOException, DocumentException {
		PdfContentByte over = writer.getDirectContent();
		document.newPage();
		//Add SessionID
		over.beginText();
		BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, BaseFont.NOT_EMBEDDED);
		over.setFontAndSize(bf, 12);
		over.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
		over.showTextAligned(Element.ALIGN_CENTER, sessionID, 510 + xShift, 143+yShift, 0);
		over.endText();
		//Add Barcode
		BarcodeQRCode qrcode = new BarcodeQRCode(barCode, 140, 140, null);
		Image img = qrcode.getImage();
		img.setAbsolutePosition(440 + xShift, 175 + yShift);
		document.add(img);	
		document.close();
    } 
 }