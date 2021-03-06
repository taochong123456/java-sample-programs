/* Copyright  (c) 2002 Graz University of Technology. All rights reserved.
 *
 * Redistribution and use in  source and binary forms, with or without 
 * modification, are permitted  provided that the following conditions are met:
 *
 * 1. Redistributions of  source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in  binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 * 
 *    "This product includes software developed by IAIK of Graz University of
 *     Technology."
 * 
 *    Alternately, this acknowledgment may appear in the software itself, if 
 *    and wherever such third-party acknowledgments normally appear.
 *  
 * 4. The names "Graz University of Technology" and "IAIK of Graz University of
 *    Technology" must not be used to endorse or promote products derived from 
 *    this software without prior written permission.
 *  
 * 5. Products derived from this software may not be called 
 *    "IAIK PKCS Wrapper", nor may "IAIK" appear in their name, without prior 
 *    written permission of Graz University of Technology.
 *  
 *  THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE LICENSOR BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY  OF SUCH DAMAGE.
 */

package demo.pkcs.pkcs11;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;

import iaik.pkcs.pkcs11.Mechanism;
import iaik.pkcs.pkcs11.Module;
import iaik.pkcs.pkcs11.Session;
import iaik.pkcs.pkcs11.Slot;
import iaik.pkcs.pkcs11.Token;
import iaik.pkcs.pkcs11.objects.Object;
import iaik.pkcs.pkcs11.objects.RSAPrivateKey;



/**
 * This program signs a given file. Hereby the hash is calculated on the card.
 * If you are not sure if your token supports this mechanism, start the
 * GetInfo demo.
 *
 * @author <a href="mailto:Karl.Scheibelhofer@iaik.at"> Karl Scheibelhofer </a>
 * @version 0.1
 * @invariants
 */
public class HashAndSign {

  static PrintWriter output_;

  static {
    try {
      //output_ = new PrintWriter(new FileWriter("HashAndSign_output.txt"), true);
      output_ = new PrintWriter(System.out, true);
    } catch (Throwable thr) {
      thr.printStackTrace();
      output_ = new PrintWriter(System.out, true);
    }
  }

  public static void main(String[] args) {
    if ((args.length != 3) && (args.length != 4)) {
      printUsage();
      System.exit(1);
    }

    try {

        Module pkcs11Module = Module.getInstance(args[0]);
        pkcs11Module.initialize(null);

        Slot[] slots = pkcs11Module.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);

        if (slots.length == 0) {
          output_.println("No slot with present token found!");
          System.exit(0);
        }

        Slot selectedSlot = slots[0];
        Token token = selectedSlot.getToken();

        Session session =
            token.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION, null, null);
        session.login(Session.UserType.USER, args[1].toCharArray());

        output_.println("################################################################################");
        output_.println("find private signature key");
        RSAPrivateKey templateSignatureKey = new RSAPrivateKey();
        templateSignatureKey.getSign().setBooleanValue(Boolean.TRUE);

        session.findObjectsInit(templateSignatureKey);

        Object[] foundSignatureKeyObjects = session.findObjects(1); // find first

        RSAPrivateKey signatureKey = null;
        if (foundSignatureKeyObjects.length > 0) {
          signatureKey = (RSAPrivateKey) foundSignatureKeyObjects[0];
          output_.println("________________________________________________________________________________");
          output_.println(signatureKey);
          output_.println("________________________________________________________________________________");
       } else {
          output_.println("No RSA private key found that can sign!");
          System.exit(0);
        }
        session.findObjectsFinal();

        output_.println("################################################################################");


        output_.println("################################################################################");
        output_.println("signing data from file: " + args[2]);

        InputStream dataInputStream = new FileInputStream(args[2]);

        // to buffer the data to be signed
        ByteArrayOutputStream dataToBeSignedBuffer = new ByteArrayOutputStream(128);

        //be sure that your token can process the specified mechanism
        Mechanism signatureMechanism = Mechanism.SHA1_RSA_PKCS;
        // initialize for signing
        session.signInit(signatureMechanism, signatureKey);

        byte[] dataBuffer = new byte[1024];
        byte[] helpBuffer;
        int bytesRead;

        /* We use the sign(byte]) function, because some drivers do not support
         * signing multiple data pieces. To buffer the data,
         * feed all data from the input stream to the data buffer strem.
         */
        while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
          dataToBeSignedBuffer.write(dataBuffer, 0, bytesRead);
        }
        byte[] dataToBeSigned = dataToBeSignedBuffer.toByteArray();

        // This signing operation is implemented in most of the drivers
        byte[] signatureValue = session.sign(dataToBeSigned);

        output_.println("The siganture value is: " + new BigInteger(1, signatureValue).toString(16));

        if (args.length == 4) {
          output_.println("Writing signature to file: " + args[3]);

          OutputStream signatureOutput = new FileOutputStream(args[3]);
          signatureOutput.write(signatureValue);
          signatureOutput.flush();
          signatureOutput.close();
        }

        output_.println("################################################################################");

        session.closeSession();
        pkcs11Module.finalize(null);

    } catch (Throwable thr) {
      thr.printStackTrace();
    } finally {
      output_.close();
    }
  }

  public static void printUsage() {
    output_.println("Usage: HashAndSign <PKCS#11 module> <userPIN> <file to be signed> [<signature value file>]");
    output_.println(" e.g.: HashAndSign pk2priv.dll password data.dat signature.bin");
    output_.println("The given DLL must be in the search path of the system.");
  }

}