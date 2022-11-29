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
		S7commManager S7c = new S7commManager("192.168.100.222");
                
                boolean end = false;
                boolean g = false;
                boolean s = false;
                
                while (!end)
                {
                    S7c.SendMessageToPLC();
                    byte[] br = S7c.ReadDBfromPLC();
                    S7commManager.PLCstatusWordElements S = S7c.DecodePLCstatusWord(br[0], br[1]);
                    
                    System.out.println("PLC status:");
                    if (S.VP2_uklop)
                        System.out.println("VP2 uklopljen");
                    else
                        System.out.println("VP2 isklopljen");
                    
                    if (S.GEN_u_zaletu)
                        System.out.println("Generator u zaletu");
                    else
                        System.out.println("Generator nije u zaletu");
                    
                    if (S.GEN_pod_teretom)
                        System.out.println("Generator pod teretom");
                    else
                        System.out.println("Generator nije pod teretom");
                    
                    
                    System.out.println("Naredba GEN:  " + String.valueOf(g));
                    System.out.println("naredba SYNC: " + String.valueOf(s));
                    System.out.println("Naredba? g/s/q");
                    
                    byte[] b = new byte[1];
                    System.in.read(b);
                    
                    switch (b[0]){
                        case 'g':
                            g = !g;
                            S7c.SetGeneratorBit(g);
                            break;
                        
                        case 's':
                            s = !s;
                            S7c.SetSynchroBit(s);
                            break;
                            
                        case 'q':
                            end = true;
                            break;
                    }
                    
                    
                            
                    
                }
                
	}

}
