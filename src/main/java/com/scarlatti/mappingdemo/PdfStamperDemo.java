package com.scarlatti.mappingdemo;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.FileOutputStream;

/**
 * @author Alessandro Scarlatti
 * @since Sunday, 7/21/2019
 */
public class PdfStamperDemo {

    public static void main(String[] args) throws Exception {
        PdfReader pdfReader = new PdfReader("sandbox/original.pdf");
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("sandbox/modified.pdf"));
        pdfStamper.getWriter().setPageEvent(new MyFooter());

        PdfAnnotation popup = PdfAnnotation.createPopup(pdfStamper.getWriter(), new Rectangle(100, 100, 100, 100), "stuff and things", true);
        pdfStamper.addAnnotation(popup, 1);
        pdfStamper.addFileAttachment(null, "stuff and things".getBytes(), null, "stuff.xml");

        int n = pdfReader.getNumberOfPages();
        for (int i = 1; i <= n; i++) {
            //add header
            PdfPTable table = new PdfPTable(1);
            table.setTotalWidth(pdfReader.getPageSize(1).getWidth()-(50*2));
            table.setLockedWidth(true);
            table.getDefaultCell().setFixedHeight(20);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell("header and things");
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell("");
            table.writeSelectedRows(0, -1, 50, 50, pdfStamper.getOverContent(i));
        }

        pdfStamper.close();
        pdfReader.close();
    }

    static class MyFooter extends PdfPageEventHelper {
        Font ffont = new Font(Font.UNDEFINED, 5, Font.ITALIC);

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase("this is a header2", ffont);
            Phrase footer = new Phrase("this is a footer2", ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                header,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.top() + 20, 0);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                footer,
                (document.right() - document.left()) / 2 + document.leftMargin(),
                document.bottom() - 20, 0);
        }
    }
}
