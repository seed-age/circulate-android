/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
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

package com.ecology.view.seedland.circulate.modle.net.webdownload;

/**
 * @author cenxiaozhong
 * @date 2018/2/11
 */
public class DownloadListenerAdapter implements DownloadListener, DownloadingListener {


    @Override
    public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
        return false;
    }

    @Override
    public void onBindService(String url, DownloadingService downloadingService) {

    }

    @Override
    public void onProgress(String url, long downloaded, long length, long usedTime) {

    }

    @Override
    public void onUnbindService(String url, DownloadingService downloadingService) {

    }

    @Override
    public boolean onResult(String path, String url, Throwable e) {
        return false;
    }
}