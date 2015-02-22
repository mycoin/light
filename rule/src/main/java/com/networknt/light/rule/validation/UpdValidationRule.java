/*
 * Copyright 2015 Network New Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.networknt.light.rule.validation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.networknt.light.rule.Rule;
import com.networknt.light.rule.transform.AbstractTransformRule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steve on 21/02/15.
 */
public class UpdValidationRule extends AbstractValidationRule implements Rule {
    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>)objects[0];
        Map<String, Object> data = (Map<String, Object>)inputMap.get("data");
        Map<String, Object> payload = (Map<String, Object>) inputMap.get("payload");
        Map<String, Object> user = (Map<String, Object>)payload.get("user");
        String error = null;
        String host = (String)user.get("host");
        String ruleClass = (String)data.get("ruleClass");
        String schema = (String)data.get("schema");
        if(host != null) {
            // admin or ruleAdmin update validation schema for their site.
            if(!host.equals(data.get("host"))) {
                error = "User can only update validation schema from host: " + host;
                inputMap.put("responseCode", 403);
            } else {
                // check if the ruleClass belongs to the host.
                if(!ruleClass.contains(host)) {
                    // you are not allowed to update transform rule to the rule as it is not owned by the host.
                    error = "ruleClass is not owned by the host: " + host;
                    inputMap.put("responseCode", 403);
                } else {
                    String json = getValidation(ruleClass);
                    if(json == null) {
                        error = "Validation schema does not exist";
                        inputMap.put("responseCode", 404);
                    } else {
                        Map<String, Object> jsonMap = mapper.readValue(json,
                                new TypeReference<HashMap<String, Object>>() {
                                });
                        if(jsonMap.get("createUserId") == null) {
                            // this validation rule is populated from form add/update/import and should not be updated
                            error = "schema is populated from form rules and cannot update";
                            inputMap.put("responseCode", 400);
                        } else {
                            Map eventMap = getEventMap(inputMap);
                            Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                            inputMap.put("eventMap", eventMap);
                            eventData.put("ruleClass", data.get("ruleClass"));
                            eventData.put("schema", data.get("schema"));
                            eventData.put("updateDate", new java.util.Date());
                            eventData.put("updateUserId", user.get("userId"));
                        }
                    }
                }
            }
        } else {
            String json = getValidation(ruleClass);
            if(json == null) {
                error = "Validation schema does not exist";
                inputMap.put("responseCode", 404);
            } else {
                Map<String, Object> jsonMap = mapper.readValue(json,
                        new TypeReference<HashMap<String, Object>>() {
                        });
                if(jsonMap.get("createUserId") == null) {
                    // this validation rule is populated from form add/update/import and should not be updated
                    error = "schema is populated from form rules and cannot update";
                    inputMap.put("responseCode", 400);
                } else {
                    Map eventMap = getEventMap(inputMap);
                    Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                    inputMap.put("eventMap", eventMap);
                    eventData.put("ruleClass", data.get("ruleClass"));
                    eventData.put("schema", data.get("schema"));
                    eventData.put("updateDate", new java.util.Date());
                    eventData.put("updateUserId", user.get("userId"));
                }
            }
        }

        if(error != null) {
            inputMap.put("error", error);
            return false;
        } else {
            return true;
        }
    }
}
