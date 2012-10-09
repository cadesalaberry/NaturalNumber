package a1posted;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;
import java.math.BigInteger;

public class TesterGrading {

	static ArrayList<int []> firstNumbers;
	static ArrayList<int []> secondNumbers;
	static ArrayList<Integer> bases;

	// Here we convert an array of integers to a BigInteger Number

	private static BigInteger convertToBigInteger(int[] number, int base){
		BigInteger bi1 = new BigInteger("0");
        StringBuffer sbfNumbers = new StringBuffer();
        if(number.length > 0){
                for(int i=0; i < number.length; i++){
                	// we are checking for numbers in bases bigger than 10
                	if(number[i]<10)
                        sbfNumbers.append(number[i]);
                	else
                		sbfNumbers.append((char)(97+number[i]-10));
                }
        }
        bi1 = new BigInteger(sbfNumbers.toString(), base);
        return bi1;
	}	
	

	// Converting a NaturalNumber String to a BigInteger String
	private static String naturalNumberToString(String NumberStr){
		String returnString = new String();
		String[] splittedString;
		int i = 0;
		//removing "," , "[" and "]" from the string
		NumberStr = NumberStr.replace('[', ',');
		NumberStr=NumberStr.replaceAll(",","");
		NumberStr=NumberStr.replaceAll("]","");
		
		splittedString = NumberStr.split(" ");

		for(i=0;i<splittedString.length;i++){
			int number = Integer.parseInt(splittedString[i]);
        	// we are checking for numbers in bases bigger than 10
			if(number>=10){
				splittedString[i] = Character.toString((char)((int)'a'+number-10));
			}
			returnString = returnString+splittedString[i];
		}
		return returnString;

	}
	
	// Generating testcase
	// Numbers are 50 and 40 digits long
	// Numbers are generated in 3 different bases
	// We generate all combinations of biggest and smallest possible numbers in each base to check carries and borrows
	// A random test case is also generated
	
	private static void generateTestCases(){
		firstNumbers = new ArrayList<int[]>();
		secondNumbers = new ArrayList<int[]>();
		bases = new ArrayList<Integer>();
		
		// you can change the bases here
		int [] basesArray = {2,10,17};
		
		int [] firstNum;
		int [] secondNum;
		int [] zero = {0};
		Random rand = new Random();
		for (int i =0 ; i< basesArray.length ; i++){

			// number of digits of first and second
            // Make them really big (bigger than can be represented with type "long")
			
			int  firstLength = 50;
			int  secondLength = 40;

			firstNum = new int[firstLength];
			secondNum = new int[secondLength];
			
			//  TEST 
			// use the biggest possible numbers for the given lengths i.e. 999999999...999 in base 10			

			for(int j = 0 ; j<firstLength ; j++){
				firstNum[j]=basesArray[i]-1;
			}
			for(int j = 0 ; j<secondLength ; j++){
				secondNum[j]=basesArray[i]-1;
			}
			firstNumbers.add(firstNum);
			secondNumbers.add(secondNum);
			bases.add(basesArray[i]);

			//  TEST 
			// use firstNum (9999999...99999 in base 10) for both first number and second number 

			firstNumbers.add(firstNum);
			secondNumbers.add(firstNum);
			bases.add(basesArray[i]);	
			
			//  TEST 			
			// use the smallest possible numbers for the given lengths i.e.  1000000...00000			

			for(int j = 1 ; j<50 ; j++){
				firstNum[j]=0;
			}
			for(int j = 1 ; j<40 ; j++){
				secondNum[j]=0;
			}
			firstNum[0]= 1;
			secondNum[0]=1;
			firstNumbers.add(firstNum);
			secondNumbers.add(secondNum);
			bases.add(basesArray[i]);	
			
			//  TEST 
			// make two random numbers
			
			for(int j = 1 ; j<50 ; j++){
				firstNum[j]=Math.abs(rand.nextInt()%basesArray[i]);
			}
			for(int j = 1 ; j<40 ; j++){
				secondNum[j]=Math.abs(rand.nextInt()%basesArray[i]);
			}
			
			firstNumbers.add(firstNum);
			secondNumbers.add(secondNum);
			bases.add(basesArray[i]);			
			
			//   Use the (random) firstNum for both numbers.
			
			firstNumbers.add(firstNum);
			secondNumbers.add(firstNum);
			bases.add(basesArray[i]);
			
			//   One random number and zero
			
			firstNumbers.add(firstNum);
			secondNumbers.add(zero);
			bases.add(basesArray[i]);			

			//   both numbers are zero

			firstNumbers.add(zero);
			secondNumbers.add(zero);
			bases.add(basesArray[i]);			
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		String correctAnswer;
		String StudentAnswer;
		int i;
		int wrongAdds=0,wrongSubs=0,wrongMults=0,wrongDivs=0; 
		NaturalNumber  firstNum = null  ;
		NaturalNumber  secondNum = null ;		
		NaturalNumber  answer;		

		//  For sanity sake,  we define number coefficients for the test code to
		//   be in the usual human readable order:
		//   { a[N-1],  a[N-2],  ...,  a[1],  a[0] }
		//  The NaturalNumber constructor  will reverse the order of these 
		//   coefficients when making the list which will make indexing easier.

		generateTestCases();
		
		System.out.println("TEST FOR ADDITION");

		for(i=0;i<firstNumbers.size();i++){
			// we generate natural numbers of our test cases
			firstNum  = new NaturalNumber(bases.get(i),firstNumbers.get(i));
			secondNum = new NaturalNumber(bases.get(i),secondNumbers.get(i));
			// we generate the biginteger representation of test case
			BigInteger bi1 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			BigInteger bi2 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			// doing the operation on biginteger numbers 
			bi1 = bi1.add(bi2);
			
			// generating strings  
			
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.add( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongAdds ++;
					System.out.println( "addition" );
					System.out.println( "First Number\t:" + firstNum.toString() );
					System.out.println( "Second Number\t:" +secondNum.toString() );
					System.out.println( "Your Answer\t:" +StudentAnswer );
					System.out.println( "Correct Answer\t:" +correctAnswer );
					System.out.println( "Base\t\t:" +bases.get(i).toString());
				}
			}
			catch(Exception e){
				wrongAdds ++;
				System.out.println( "addition" );
				System.out.println( "First Number\t:" + firstNum.toString() );
				System.out.println( "Second Number\t:" +secondNum.toString() );
				System.out.println( "Your Answer\t: Exception" );
				System.out.println( "Correct Answer\t:" +correctAnswer );
				System.out.println( "Base\t\t:" +bases.get(i).toString());
				
			
			}
			// swapping first number and second number for addition
			answer = firstNum;
			firstNum= secondNum;
			secondNum= answer;
			
			bi1 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			bi2 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			bi1 = bi1.add(bi2);
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.add( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongAdds ++;
					System.out.println( "addition" );
					System.out.println( "First Number:" + firstNum.toString() );
					System.out.println( "Second Number:" +secondNum.toString() );
					System.out.println( "Your Answer:" +StudentAnswer );
					System.out.println( "Correct Answer:" +correctAnswer );
					System.out.println( "Base:" +bases.get(i).toString());
	
				}
			}
			catch(Exception e){
				wrongAdds ++;
				System.out.println( "addition" );
				System.out.println( "First Number:" + firstNum.toString() );
				System.out.println( "Second Number:" +secondNum.toString() );
				System.out.println( "Your Answer: Exception" );
				System.out.println( "Correct Answer:" +correctAnswer );
				System.out.println( "Base:" +bases.get(i).toString());
			}
		}
		System.out.println( "Addition: "+wrongAdds+" wrong answers in " +i*2+" test cases." );

		
		System.out.println("TEST FOR MULTIPLICATION");
		for(i=0;i<firstNumbers.size();i++){
			firstNum  = new NaturalNumber(bases.get(i),firstNumbers.get(i));
			secondNum = new NaturalNumber(bases.get(i),secondNumbers.get(i));	
			BigInteger bi1 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			BigInteger bi2 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			bi1 = bi1.multiply(bi2);
			
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.multiply( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongMults ++;
					System.out.println( "MULTIPLICATION" );
					System.out.println( "First Number:" + firstNum.toString() );
					System.out.println( "Second Number:" +secondNum.toString() );
					System.out.println( "Your Answer:" +StudentAnswer );
					System.out.println( "Correct Answer:" +correctAnswer );
					System.out.println( "Base:" +bases.get(i).toString());
				}
			}
			catch(Exception e){
				wrongMults ++;
				System.out.println( "MULTIPLICATION" );
				System.out.println( "First Number:" + firstNum.toString() );
				System.out.println( "Second Number:" +secondNum.toString() );
				System.out.println( "Your Answer: Exception" );
				System.out.println( "Correct Answer:" +correctAnswer );
				System.out.println( "Base:" +bases.get(i).toString());
			}
			answer = firstNum;
			firstNum= secondNum;
			secondNum= answer;
		
			bi1 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			bi2 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			bi1 = bi1.multiply(bi2);
			
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.multiply( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongMults ++;
					System.out.println( "MULTIPLICATION" );
					System.out.println( "First Number\t:" + firstNum.toString() );
					System.out.println( "Second Number\t:" +secondNum.toString() );
					System.out.println( "Your Answer\t:" +StudentAnswer );
					System.out.println( "Correct Answer\t:" +correctAnswer );
					System.out.println( "Base\t\t:" +bases.get(i).toString());
	
				}
			}
			catch(Exception e){
				wrongMults ++;
				System.out.println( "MULTIPLICATION" );
				System.out.println( "First Number\t:" + firstNum.toString() );
				System.out.println( "Second Number\t:" +secondNum.toString() );
				System.out.println( "Your Answer\t: Exception" );
				System.out.println( "Correct Answer\t:" +correctAnswer );
				System.out.println( "Base\t\t:" +bases.get(i).toString());
			}
		}		
		System.out.println( "Multiplication: "+wrongMults+" wrong answers in " +i*2+" test cases." );
		
		System.out.println("TEST FOR SUBTRACTION");
		for(i=0;i<firstNumbers.size();i++){
			firstNum  = new NaturalNumber(bases.get(i),firstNumbers.get(i));
			secondNum = new NaturalNumber(bases.get(i),secondNumbers.get(i));	
			BigInteger bi1 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			BigInteger bi2 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			bi1 = bi1.subtract(bi2);
			
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.subtract( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongSubs ++;
					System.out.println( "SUBTRACTION" );
					System.out.println( "First Number\t:" + firstNum.toString() );
					System.out.println( "Second Number\t:" +secondNum.toString() );
					System.out.println( "Your Answer\t:" +StudentAnswer );
					System.out.println( "Correct Answer\t:" +correctAnswer );
					System.out.println( "Base\t\t:" +bases.get(i).toString());
				}
			}
			catch(Exception e){
				wrongSubs ++;
				System.out.println( "SUBTRACTION" );
				System.out.println( "First Number\t:" + firstNum.toString() );
				System.out.println( "Second Number\t:" +secondNum.toString() );
				System.out.println( "Your Answer\t: Exception" );
				System.out.println( "Correct Answer\t:" +correctAnswer );
				System.out.println( "Base\t\t:" +bases.get(i).toString());
			}

		}		
		System.out.println( "Subtraction: "+wrongSubs+" wrong answers in " +i+" test cases." );
		
		System.out.println("TEST FOR DIVISION");		
		for( i=0;i<firstNumbers.size();i++){
			// we check if any of the numbers are zero and we ignore it
			if(firstNumbers.get(i).length==1||secondNumbers.get(i).length==1)
				continue;
			firstNum  = new NaturalNumber(bases.get(i),firstNumbers.get(i));
			secondNum = new NaturalNumber(bases.get(i),secondNumbers.get(i));	
			BigInteger bi1 = convertToBigInteger(firstNumbers.get(i), bases.get(i));
			BigInteger bi2 = convertToBigInteger(secondNumbers.get(i), bases.get(i));
			bi1 = bi1.divide(bi2);
			
			correctAnswer = bi1.toString(bases.get(i));
			try{
				StudentAnswer = firstNum.divide( secondNum ).toString();
				StudentAnswer = naturalNumberToString(StudentAnswer);
				// checking the answer
				if(!StudentAnswer.equalsIgnoreCase( correctAnswer) ){
					wrongDivs ++;
					System.out.println( "Division:" );
					System.out.println( "First Number\t:" + firstNum.toString() );
					System.out.println( "Second Number\t:" +secondNum.toString() );
					System.out.println( "Your Answer\t:" +StudentAnswer );
					System.out.println( "Correct Answer\t:" +correctAnswer );
					System.out.println( "Base\t\t:" +bases.get(i).toString());
				}
			}
			catch(Exception e){
				wrongDivs ++;
				System.out.println( "Division" );
				System.out.println( "First Number\t:" + firstNum.toString() );
				System.out.println( "Second Number\t:" +secondNum.toString() );
				System.out.println( "Your Answer\t: Exception" );
				System.out.println( "Correct Answer\t:" +correctAnswer );
				System.out.println( "Base\t\t:" +bases.get(i).toString());
			}
		}
		System.out.println( "Division "+wrongDivs+" wrong answers in " +i+" test cases." );

	
	}

}

