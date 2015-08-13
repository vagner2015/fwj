package br.com.smartsy.fwj.web.soap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.headers.Header;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SoapHeader {
	
	private List<Header> headers = new ArrayList<>();
	
	public SoapHeader(SoapMessage message) {
		this.headers = message.getHeaders();
	}
	
	public List<Header> getHeaders() {
		return headers;
	}
	
	public Header getHeader(String name){
		for(Header header : getHeaders()){
			QName qname = header.getName();
			if(qname.getLocalPart().toLowerCase().equals(name.toLowerCase()))
				return header;
		}
		return null;
	}
	
	public BasicAuthHeader getBasicAuthHeader(){
		Header header = getHeader(BasicAuthHeader.TAG_HEADER);
		if(header != null){
			Element element = (Element)header.getObject();
			NodeList childs = element.getChildNodes();
			int aux = 0;
			int length = childs.getLength();
			BasicAuthHeader auth = new BasicAuthHeader();
            while(aux < length){
            	Node item = childs.item(aux++);
            	if(item.getNodeName().toLowerCase().equals(BasicAuthHeader.TAG_USERNAME)){
            		auth.setUsername(item.getTextContent());
            		continue;
            	}
            	if(item.getNodeName().toLowerCase().equals(BasicAuthHeader.TAG_PASSWORD))
            		auth.setUsername(item.getTextContent());
            }
            return auth;
		}
		return null;
	}
	
	public TokenAuthHeader getTokenAuthHeader(){
		Header header = getHeader(TokenAuthHeader.TAG_HEADER);
		if(header != null){
			Element element = (Element)header.getObject();
			NodeList childs = element.getChildNodes();
			int aux = 0;
			int length = childs.getLength();
			TokenAuthHeader auth = new TokenAuthHeader();
            while(aux < length){
            	Node item = childs.item(aux++);
            	if(item.getNodeName().toLowerCase().equals(TokenAuthHeader.TAG_TOKEN)){
            		auth.setToken(item.getTextContent());
            		break;
            	}
            }
            return auth;
		}
		return null;
	}
	
}