/*
 * Copyright 2015 VMware, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, without warranties or
 * conditions of any kind, EITHER EXPRESS OR IMPLIED.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.vmware.photon.controller.common.xenon.validation;

import com.vmware.xenon.common.ServiceDocument;

import org.apache.commons.lang3.StringUtils;
import static com.google.common.base.Preconditions.checkState;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * This class implements a validator that checks if the {@link NotBlank}
 * annotation on a state object is honored.
 */
public enum NotBlankValidator {
  INSTANCE;

  public static void validate(ServiceDocument state) {
    try {
      Field[] declaredFields = state.getClass().getDeclaredFields();
      for (Field field : declaredFields) {
        Annotation[] declaredAnnotations = field.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
          if (annotation.annotationType() == NotBlank.class) {
            checkState(null != field.get(state), String.format("%s cannot be null", field.getName()));
            if (String.class.equals(field.getType())) {
              checkState((StringUtils.isNotBlank((String) field.get(state))),
                  String.format("%s cannot be blank", field.getName()));
            }
          }
        }
      }
    } catch (IllegalStateException e) {
      throw e;
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
