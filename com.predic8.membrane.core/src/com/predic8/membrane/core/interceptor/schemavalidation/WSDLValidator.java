package com.predic8.membrane.core.interceptor.schemavalidation;

import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Source;

import com.predic8.membrane.core.Router;
import com.predic8.membrane.core.http.Response;
import com.predic8.membrane.core.util.HttpUtil;
import com.predic8.membrane.core.util.MessageUtil;
import com.predic8.schema.Schema;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.WSDLParserContext;
import com.predic8.xml.util.ResourceDownloadException;

public class WSDLValidator extends AbstractXMLValidator {

	public WSDLValidator(Router router, String location) throws Exception {
		super(router, location);
	}
	
	protected List<Schema> getSchemas() {
		WSDLParserContext ctx = new WSDLParserContext();
		ctx.setInput(location);
		try {
			return new WSDLParser().parse(ctx).getTypes().getSchemas();
		} catch (ResourceDownloadException e) {
			throw new IllegalArgumentException("Could not download the WSDL " + location + " or its dependent XML Schemas.");
		}
	}
	
	protected Source getMessageBody(InputStream input) throws Exception {
		return MessageUtil.getSOAPBody(input);
	}
	
	@Override
	protected Response createErrorResponse(String message) {
		return HttpUtil.createSOAPValidationErrorResponse(message);
	}


}