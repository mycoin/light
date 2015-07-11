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
var keyMirror = require('keymirror');

var APIRoot = "http://localhost:3000";

module.exports = {

    APIEndpoints: {
        LOGIN:          APIRoot + "/v1/login",
        REGISTRATION:   APIRoot + "/v1/users",
        STORIES:        APIRoot + "/v1/stories"
    },

    ActionTypes: keyMirror({
        // Auth
        LOGIN_REQUEST: null,
        LOGIN_RESPONSE: null,
        LOGOUT: null,
        SIGNUP_REQUEST: null,


        // Routes
        REDIRECT: null,

        // Blog
        LOAD_STORIES: null,
        RECEIVE_STORIES: null,
        LOAD_STORY: null,
        RECEIVE_STORY: null,
        CREATE_STORY: null,
        RECEIVE_CREATED_STORY: null
    })

};