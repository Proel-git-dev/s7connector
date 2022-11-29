/*
 * Copyright 2022 pavle.sprljan.
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
package com.github.s7connector.test.example;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;
import java.io.IOException;

/**
 *
 * @author pavle.sprljan
 */
public class S7commManager {
    
    S7Connector S7conn = null;
    
    private boolean GeneratorBit = false;
    private boolean SynchroBit = false;
    
    public class PLCstatusWordElements
    {
        boolean VP2_uklop;
        boolean GEN_u_zaletu;
        boolean GEN_pod_teretom;
    }
    
    S7commManager(String PLCaddress) throws IOException
    { 
        S7conn = S7ConnectorFactory
			.buildTCPConnector()
			.withHost(PLCaddress)
			.withRack(0)
			.withSlot(0)
			.build();
        
        if (S7conn == null)
        {
            throw new IOException("No connection to PLC");
        }
    }
    
    protected void finalize() throws IOException
    {
        if (S7conn != null)
                {
                    S7conn.close();
                }
    }
    
    public void SetGeneratorBit(boolean v)
    {
        GeneratorBit = v;
    }
    
    public void SetSynchroBit(boolean v)
    {
        SynchroBit = v;
    }
    
    public void SendMessageToPLC() throws IOException
    {
        byte[] msg = new byte[32];
        
        // bajtovi rijeci su zamijenjeni
        // dakle pisemo na index 1
        
        if (GeneratorBit)
        {
            msg[1] = (byte)0x01;
        }
        
        if (SynchroBit)
        {
            msg[1] = (byte)(msg[0] | 0x02);
        }
        
        if (S7conn != null)
        {
            S7conn.write(DaveArea.DB, 51, 0, msg);
        }
        else
        {
            throw new IOException("No connection to PLC");
        }
        
    }
    
    public byte[] ReadDBfromPLC() throws IOException
    {
        byte[] br = new byte[32];
        if (S7conn != null)
        {
            br = S7conn.read(DaveArea.DB, 50, 32, 0);
        }
        else
        {
            throw new IOException("No connection to PLC");
        }
        
        return br;
    }
    
    public PLCstatusWordElements DecodePLCstatusWord(byte b0, byte b1)
    {
        PLCstatusWordElements S = new PLCstatusWordElements();
        
        S.VP2_uklop = (b1 & 0x01) != 0;
        S.GEN_u_zaletu = (b1 & 0x02) != 0;
        S.GEN_pod_teretom = (b1 & 0x04) != 0;
        
        return S;
    }
    
}
