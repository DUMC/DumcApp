package my.dumc.dumc;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Aaron David on 8/10/2017.
 */

public class XMLParser
{
    public Element XMLParserString(String xml) throws ParserConfigurationException, IOException, SAXException
    {
        String getXML = xml;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(getXML)));
        //Document document = builder.parse(new File("file.xml"));
        Element rootElement = document.getDocumentElement();
        return rootElement;
    }

    protected String getString(String tagName, Element element)
    {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0)
        {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0)
            {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

    protected void writeToFile(String filename, String response, Context context) throws IOException
    {
        FileOutputStream fileOutputStream = context.openFileOutput(filename, context.MODE_PRIVATE);
        fileOutputStream.write(response.getBytes());
        fileOutputStream.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String sendFileRequest(String urlString, Context context) throws IOException
    {
        final File uploadFile = new File(context.getFilesDir().getAbsoluteFile() + "/create_attendance.xml");

        URL url = new URL(urlString);
        MultipartUtility multipartUtility = new MultipartUtility(urlString);
        multipartUtility.addFilePart("create_attendance",uploadFile);
        byte[] bytes = multipartUtility.finish();
        String result = new String(bytes, StandardCharsets.UTF_8);

        return result;
    }
}
