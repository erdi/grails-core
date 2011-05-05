/*
 * Copyright 2011 SpringSource
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.plugins.web.api;

import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.web.binding.DataBindingLazyMetaPropertyMap;
import org.codehaus.groovy.grails.web.binding.DataBindingUtils;
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest;
import org.springframework.validation.BindingResult;

import java.util.Map;

/**
 * Enhancements made to domain classes to for data binding
 *
 * @author Graeme Rocher
 * @since 1.4
 */
public class ControllersDomainBindingApi {

    /**
     * A map based constructor that binds the named arguments to the target instance
     *
     * @param instance The target instance
     * @param namedArgs The named arguments
     */
    public static void initialize(Object instance, Map namedArgs) {
        GrailsDomainClass dc = getDomainClass(instance);
        if(dc != null) {
            DataBindingUtils.bindObjectToDomainInstance(dc, instance, namedArgs);
            DataBindingUtils.assignBidirectionalAssociations(instance, namedArgs, dc);
        }
    }

    /**
     * Binds the source object to the properties of the target instance converting any types as necessary
     *
     * @param instance The instance
     * @param bindingSource The binding source
     * @return The BindingResult
     */
    public BindingResult setProperties(Object instance, Object bindingSource) {
        GrailsDomainClass dc = getDomainClass(instance);
        if(dc != null) {
            return DataBindingUtils.bindObjectToDomainInstance(dc, instance, bindingSource);
        }
        return null;
    }

    /**
     * Returns a map of the objects properties that can be used to during binding to bind a subset of properties
     *
     * @param instance The instance
     * @return An instance of {@link DataBindingLazyMetaPropertyMap}
     */
    public Map getProperties(Object instance) {
        return new DataBindingLazyMetaPropertyMap(instance);
    }


    private static GrailsDomainClass getDomainClass(Object instance) {
        GrailsWebRequest webRequest = GrailsWebRequest.lookup();
        if(webRequest != null) {
            GrailsApplication grailsApplication = webRequest.getApplicationContext().getBean(GrailsApplication.APPLICATION_ID, GrailsApplication.class);
            if(grailsApplication != null) {
                return (GrailsDomainClass) grailsApplication.getArtefact(DomainClassArtefactHandler.TYPE, instance.getClass().getName());
            }
        }
        return null;
    }
}