package lt.vikoeif.lzlatkus.opticrecieverslave;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

public class Optic {
    Tesseract tesseract = new Tesseract();

    public String textExtractor(File file) {
        String text = "";

        try {
            tesseract.setDatapath("C:\\Users\\s033860\\Desktop\\Tess4J-3.4.8-src\\Tess4J\\tessdata");

            text = tesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return text;
    }
}
