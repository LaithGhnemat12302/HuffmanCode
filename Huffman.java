package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;


public class Huffman {
	
	public static HuffCode[] huffCodeArray;
	public static int huffCodeArraySize = 0;
	static int compressedSize;
	static int originalSize;
	public static String outFileName;

	public static void compress(File file) throws IOException {
		String filepath = file.getPath();
		FileInputStream scan = new FileInputStream(filepath);

		originalSize = scan.available();// to store size of file to do compress
		// Reading the file as bytes
		byte[] buffer = new byte[1024];              // Read file as (bytes)
		int[] tmp = new int[256];

		int size = scan.read(buffer, 0, 1024);
		int index = -1;
		do {
			for (int i = 0; i < size; i++) {
				index = buffer[i];
				if (index < 0)
					index += 256;
				if (tmp[index] == 0)
					huffCodeArraySize++;       // Counting  number of different characters in the file
				tmp[index]++;
			}
			size = scan.read(buffer, 0, 1024);
		} while (size > 0); // Finish Counting

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = 0;
		int tracker = 0;

		// Shrink the original array, building the huffCodeArray                              
		int nbChar = 0;
		
		
		
		huffCodeArray = new HuffCode[huffCodeArraySize]; // building the HuffmanCodeArray
		
		
		for (int i = 0; i < 256; i++)
			if (tmp[i] != 0) {
				huffCodeArray[tracker++] = new HuffCode((char) i, tmp[i]);// Number of Frequency
				nbChar += tmp[i];
				tmp[i] = 0;
			}                                                                   

		if (huffCodeArraySize != 1) {
			Node[] t = new Node[huffCodeArraySize];
			Heap h = new Heap(huffCodeArraySize + 10);
			for (int i = 0; i < huffCodeArraySize; i++) {
				t[i] = new Node(huffCodeArray[i].counter, huffCodeArray[i].character);
				h.addElt(t[i]);
			}                                           // Add the character and its frequency in tree node m,, then
				                                        // add the tree node to the heap

			for (int i = 1; i <= t.length - 1; i++) {
				Node z = new Node();
				Node x = h.deleteElt();
				Node y = h.deleteElt();
				z.freq = x.freq + y.freq;
				z.left = x;
				z.right = y;
				h.addElt(z);
			}                                                              // delete from heap and add a new tree node
			getCodes(h.getElt()[1], "");
		} else {
			huffCodeArray[0].huffCode = "1";
			huffCodeArray[0].codeLength = 1;
		}

		String[] out = new String[256];
		for (int i = 0; i < huffCodeArraySize; i++)
			out[(int) huffCodeArray[i].character] = new String(huffCodeArray[i].huffCode);

		outFileName = new StringTokenizer(file.getAbsolutePath(), ".").nextToken() + ".huff";
		File f = new File(outFileName);
		                       // output = new FileOutputStream(outFileName);
		FileOutputStream output = new FileOutputStream(outFileName);
		byte[] outbuffer = new byte[1024];
		tracker = 0;

		// The Name of The Original File

		for (int i = 0; i < filepath.length(); i++)
			outbuffer[tracker++] = (byte) filepath.charAt(i);
		outbuffer[tracker++] = '\n';

		// Number of Characters in the Original File
		String nbchar = String.valueOf(nbChar);
		for (int i = 0; i < nbchar.length(); i++) {
			outbuffer[tracker++] = (byte) nbchar.charAt(i);
		}
		outbuffer[tracker++] = '\n';

		// Number of Distinct Characters
		for (int i = 0; i < String.valueOf(huffCodeArraySize).length(); i++)
			outbuffer[tracker++] = (byte) String.valueOf(huffCodeArraySize).charAt(i);
		outbuffer[tracker++] = '\n';

		output.write(outbuffer, 0, tracker);
		tracker = 0;
		for (int i = 0; i < outbuffer.length; i++)
			outbuffer[i] = 0;

		// The HuffCode for Each Character
		for (int i = 0; i < huffCodeArraySize; i++) {
			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			outbuffer[tracker++] = (byte) huffCodeArray[i].character;
			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			// Add the Counter
			outbuffer[tracker++] = (byte) huffCodeArray[i].codeLength;
			String res = "";
			Long x;
			if (huffCodeArray[i].huffCode.length() > 15) {
				for (int z = 0; z < huffCodeArray[i].huffCode.length() / 2; z++) {
					res += huffCodeArray[i].huffCode.charAt(z) + "";
				}
				x = Long.parseLong(res);
				res = "";
				for (int z = (huffCodeArray[i].huffCode.length() + 1) / 2; z < huffCodeArray[i].huffCode
						.length(); z++) {
					res += huffCodeArray[i].huffCode.charAt(z) + "";
				}
				x += Long.parseLong(res);

			} else {
				x = Long.parseLong(huffCodeArray[i].huffCode);
			}
			byte[] code = new byte[50];
			int l = 0;
			if (x == 0) {
				outbuffer[tracker++] = 0;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}

				outbuffer[tracker++] = 0;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}
			} else {
				while (x != 0) {
					if (tracker == 1024) {
						output.write(outbuffer);
						tracker = 0;
					}
					code[l++] = (byte) (x % 256);
					x /= 256;
				}
				outbuffer[tracker++] = (byte) l;
				if (tracker == 1024) {
					output.write(outbuffer);
					tracker = 0;
				}
				for (int j = 0; j < l; j++) {
					outbuffer[tracker++] = code[j];
					if (tracker == 1024) {
						output.write(outbuffer);
						tracker = 0;
					}
				}
			}

			if (tracker == 1024) {
				output.write(outbuffer);
				tracker = 0;
			}
			outbuffer[tracker++] = '\n';
		} // end for
		
		
		
		
		

		// Print Out The Header
		output.write(outbuffer, 0, tracker);
		

		// Reinitialize the Output Buffer
		for (int i = 0; i < outbuffer.length; i++)
			outbuffer[i] = 0;

		scan.close();
		scan = new FileInputStream(filepath);
		tracker = 0;
		size = scan.read(buffer, 0, 1024);
		do {
			for (int i = 0; i < size; i++) {
				index = buffer[i];
				if (index < 0)// If the Value was negative
					index += 256;
				for (int j = 0; j < out[index].length(); j++) {
					char ch = out[index].charAt(j);
					if (ch == '1')
						outbuffer[tracker / 8] = (byte) (outbuffer[tracker / 8] | 1 << 7 - tracker % 8);
					tracker++;
					if (tracker / 8 == 1024) {
						output.write(outbuffer);
						for (int k = 0; k < outbuffer.length; k++)
							outbuffer[k] = 0;
						tracker = 0;
					}
				}
			}
			size = scan.read(buffer, 0, 1024);
		} while (size > 0);
		scan.close();
		output.write(outbuffer, 0, (tracker / 8) + 1);
		output.close();
		scan = new FileInputStream(f);
		compressedSize = scan.available();
		scan.close();
		
	}
	
	public static void deCompress(File infile) {
		try {

			int size = 0;
			String fileName = null;
			fileName = infile.getPath();

			int tracker = 0;
			int bufferTracker = 0;
			boolean flag = true;
			String originalFileName = "";
			File file = new File(fileName);
			
			

			FileInputStream scan = new FileInputStream(file);

			byte[] buffer = new byte[1024];

			// Get The File Name
			size = scan.read(buffer, 0, 1024);
			char[] tmp = new char[200];
			while (flag) {
				if (buffer[tracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			
			bufferTracker++;
			originalFileName = String.valueOf(tmp, 0, tracker);

			// Get the Number of Characters in the file
			long nbChar = 0;
			tracker = 0;
			flag = true;
			while (flag) {
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (buffer[bufferTracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			nbChar = Integer.parseInt(String.valueOf(tmp, 0, tracker));
			bufferTracker++;

			// Get the Number of Distinct characters
			int loopSize = 0;
			tracker = 0;
			flag = true;
			while (flag) {
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (buffer[bufferTracker] != '\n')
					tmp[tracker++] = (char) buffer[bufferTracker++];
				else
					flag = false;
			}
			loopSize = Integer.parseInt(String.valueOf(tmp, 0, tracker));
			bufferTracker++;

			// Reading the huff Code
			huffCodeArray = new HuffCode[loopSize];
			huffCodeArraySize = loopSize;
			for (int i = 0; i < loopSize; i++) {
				huffCodeArray[i] = new HuffCode((char) Byte.toUnsignedInt(buffer[bufferTracker++]));
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				huffCodeArray[i].codeLength = buffer[bufferTracker++];
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				int l = buffer[bufferTracker++];
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				if (l == 0)
					bufferTracker++;
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
				long x = 0;
				for (int j = 0; j < l; j++) {
					x += Byte.toUnsignedLong(buffer[bufferTracker++]) * (1 << 8 * j);
					if (bufferTracker == 1024) {
						size = scan.read(buffer, 0, 1024);
						bufferTracker = 0;
					}
				}
				huffCodeArray[i].huffCode = String.valueOf(x);
				if (huffCodeArray[i].huffCode.length() != huffCodeArray[i].codeLength) {
					String s = "";
					for (int j = 0; j < huffCodeArray[i].codeLength - huffCodeArray[i].huffCode.length(); j++)
						s += "0";
					s += huffCodeArray[i].huffCode;
					huffCodeArray[i].huffCode = s;
				}
				bufferTracker++;
				if (bufferTracker == 1024) {
					size = scan.read(buffer, 0, 1024);
					bufferTracker = 0;
				}
			}

			BinaryTree tree = new BinaryTree();
			for (int i = 0; i < huffCodeArraySize; i++) {
				tree = BinaryTree.addElt(tree, huffCodeArray[i].huffCode, 0, huffCodeArray[i].character);
			}
			for (int i = 0; i < tmp.length; i++)
				tmp[i] = '\0';

			if (bufferTracker == 1024) {
				size = scan.read(buffer, 0, 1024);
				bufferTracker = 0;
			}

			int index = bufferTracker;
			bufferTracker = 0;
			byte[] outputBuffer = new byte[1024];
			tracker = 0;
			String name = "";
			File n = new File(name);

			if (n.exists())
				n.delete();

			FileOutputStream output = new FileOutputStream(originalFileName);
			BinaryTree root = tree;
			long count = 0;
			flag = false;
			do {
				while (tree.left != null || tree.right != null) {
					if ((buffer[index] & (1 << 7 - bufferTracker)) == 0)
						tree = tree.left;
					else
						tree = tree.right;
					bufferTracker++;
					if (bufferTracker == 8) {
						bufferTracker = 0;
						index++;
						if (index == 1024) {
							size = scan.read(buffer, 0, 1024);
							index = 0;
							if (size == -1)
								flag = true;
						}
					}
				}
				if (flag)
					break;
				outputBuffer[tracker++] = (byte) tree.ch;
				if (tracker == 1024) {
					output.write(outputBuffer);
					tracker = 0;
				}
				count++;
				tree = root;
				if (count == nbChar)
					break;
			} while (size != -1);
			output.write(outputBuffer, 0, tracker);
			output.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void getCodes(Node t, String st) {
		if (t.left == t.right && t.right == null)
			for (int i = 0; i < huffCodeArraySize; i++) {
				if (huffCodeArray[i].character == t.ch) {
					huffCodeArray[i].huffCode = st;
					huffCodeArray[i].codeLength = st.length();
				}
			}
		else {
			getCodes(t.left, st + '0');
			getCodes(t.right, st + '1');
		}
	}
}
