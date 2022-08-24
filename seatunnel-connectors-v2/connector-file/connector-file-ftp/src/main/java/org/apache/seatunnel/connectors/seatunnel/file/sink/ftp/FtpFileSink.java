/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.connectors.seatunnel.file.sink.ftp;

import org.apache.seatunnel.api.common.PrepareFailException;
import org.apache.seatunnel.api.sink.SeaTunnelSink;
import org.apache.seatunnel.common.config.CheckConfigUtil;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.common.constants.PluginType;
import org.apache.seatunnel.connectors.seatunnel.file.sink.AbstractFileSink;
import org.apache.seatunnel.connectors.seatunnel.file.sink.ftp.config.FtpConfig;
import org.apache.seatunnel.connectors.seatunnel.file.sink.ftp.util.FtpFileUtils;
import org.apache.seatunnel.connectors.seatunnel.file.sink.spi.SinkFileSystemPlugin;

import org.apache.seatunnel.shade.com.typesafe.config.Config;

import com.google.auto.service.AutoService;

@AutoService(SeaTunnelSink.class)
public class FtpFileSink extends AbstractFileSink {

    private String ftpHost;
    private Integer ftpPort;
    private String ftpUserName;
    private String ftpPwd;

    @Override
    public SinkFileSystemPlugin getSinkFileSystemPlugin() {
        return new FtpFileSinkPlugin();
    }

    @Override
    public void prepare(Config pluginConfig) throws PrepareFailException {
        super.prepare(pluginConfig);
        CheckResult result = CheckConfigUtil.checkAllExists(pluginConfig,
                FtpConfig.FTP_HOST, FtpConfig.FTP_PORT, FtpConfig.FTP_USERNAME, FtpConfig.FTP_PASSWORD);
        if (!result.isSuccess()) {
            throw new PrepareFailException(getPluginName(), PluginType.SINK, result.getMsg());
        } else {
            this.ftpHost = pluginConfig.getString(FtpConfig.FTP_HOST);
            this.ftpPort = pluginConfig.getInt(FtpConfig.FTP_PORT);
            this.ftpUserName = pluginConfig.getString(FtpConfig.FTP_USERNAME);
            this.ftpPwd = pluginConfig.getString(FtpConfig.FTP_PASSWORD);
            FtpFileUtils.initFTPClient(this.ftpHost, this.ftpPort, this.ftpUserName, this.ftpPwd);

        }
    }
}