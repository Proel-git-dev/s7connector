/*
Copyright 2016 S7connector members (github.com/s7connector)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.github.s7connector.test.example;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;

/**
 * @author Thomas Rudin (thomas@rudin-informatik.ch)
 *
 */
public class PlainTutorialExample
{
        private static short MakeWordFromBytes(byte b1, byte b2)
        {
            return (short) (Byte.toUnsignedInt(b1) + (Byte.toUnsignedInt(b2) << 8));
        }
	
	public static void main(String[] args) throws Exception
	{
		//Open TCP Connection
		S7Connector connector = 
				S7ConnectorFactory
				.buildTCPConnector()
				.withHost("192.168.100.222")
				.withRack(0)
				.withSlot(0)
				.build();
		
		//Set some bytes
		byte[] bw = new byte[32];
                // sedamnaesti bajt nije povezan s nicim u PLC programu, sluzi samo za test
                java.util.Random rand = new java.util.Random();
                bw[16] = (byte)rand.nextInt();
		
		//Write to DB51 32 bytes
		connector.write(DaveArea.DB, 51, 0, bw);
                
                //Read from DB50 32 bytes
                // ovo je povratni slog prema serveru, sadrzavat ce informacije s PLC-a
		byte[] br = connector.read(DaveArea.DB, 50, 32, 0);
                
                
                System.out.println("Statusni bajt: " + String.format("0x%02X", br[0]));
                
                short w = MakeWordFromBytes(br[27], br[26]);
                System.out.println("Testna rijec 1: " + String.format("0x%02X", w));
                w = MakeWordFromBytes(br[29], br[28]);
                System.out.println("Testna rijec 2: " + String.format("0x%02X", w));
                
		
		//Close connection
		connector.close();
	}

}
