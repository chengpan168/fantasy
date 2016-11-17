package com.shining3d.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

/**
 * 
* @Description: 
* @version: v1.0.0
* @author: tianlg
* @date: Jan 20, 2015 3:33:18 PM
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* Jan 20, 2015      tianlg          v1.0.0
 */
public class VelocityStringResourceLoader extends ResourceLoader {

	@Override
	public void init(ExtendedProperties configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream getResourceStream(String source)
			throws ResourceNotFoundException {
        if (source == null || source.length() == 0) {  
            throw new ResourceNotFoundException("template not defined!");
        }
        return new ByteArrayInputStream(source.getBytes());  
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		// TODO Auto-generated method stub
		return 0;
	}

}
