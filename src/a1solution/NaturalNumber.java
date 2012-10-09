package a1solution;

/*
 *   STUDENT NAME      :  
 *   STUDENT ID        :
 *   
 *   If you have any issues that you wish the T.A.s to consider, then you
 *   should list them here.   If you discussed on the assignment in depth 
 *   with another student, then you should list that student's name here.   
 *   We insist that you each write your own code.   But we also expect 
 *   (and indeed encourage) that you discuss some of the technical
 *   issues and problems with each other, in case you get stuck.    

 *   
 */

import java.util.LinkedList;

public class NaturalNumber  {
	
	int	base;       

	LinkedList<Integer>  coefficients;

	//   For any base and any positive integer, the representation of that positive 
	//   integer as a sum of powers of that base is unique.  
	//   Moreover,  we require that the "last" coefficient (namely, the coefficient
	//   of the largest power)  is non-zero.  
	//   For example,  350 is a valid representation (which we call "three hundred fifty") 
	//   but 0353 is not.  
	
	//  Constructors

	//  This constructor acts as a helper.  It is not called from the Tester class.
	
	NaturalNumber(int base){
		this.base = base;
		coefficients = new LinkedList<Integer>();
	}

	//  This constructor acts as a helper.  It is not called from the Tester class.

	NaturalNumber(int base, int i) throws Exception{
		this.base = base;
		coefficients = new LinkedList<Integer>();
		
		if ((i >= 0) && (i < base))
			coefficients.addFirst( new Integer(i) );
		else {
			System.out.println("constructor error: all coefficients should be non-negative and less than base");
			throw new Exception();
		}
	}

	NaturalNumber(int base, int[] intarray) throws Exception{
		this.base = base;
		coefficients = new LinkedList<Integer>();
		for (int i=0; i < intarray.length; i++){
			if ((i >= 0) && (intarray[i] < base))
				coefficients.addFirst( new Integer( intarray[i] ) );
			else{
				System.out.println("constructor error:  all coefficients should be non-negative and less than base");
				throw new Exception();
			}
		}

		//   Remove 0's in highest order coefficients, since this is illegal format. 

		while ((this.coefficients.size() > 1) & 
				(this.coefficients.getLast().intValue() == 0)){
			this.coefficients.removeLast();
		}

	}
		
	public NaturalNumber add( NaturalNumber  second){
				
		//  initialize the sum as an empty list of coefficients
		
		NaturalNumber sum = new NaturalNumber( this.base );
	
		//   ADD YOUR CODE HERE
		
		//  ---------  BEGIN SOLUTION (add)  ----------
		//
		//   If the two numbers have a different polynomial order
		//   then pad the smaller one with zero coefficients.
		//   This also handles the case where one of the numbers is the empty list.
        //
		//   The add method shouldn't affect the numbers themselves. 
		//   So let's just work  with a copy (a clone) of the numbers.   		
		
		NaturalNumber  firstClone = this.clone();
		NaturalNumber  secondClone = second.clone();

		int diff = firstClone.coefficients.size() - second.coefficients.size();
		while (diff < 0){  // second is bigger
			firstClone.coefficients.add(0);
			diff++;
		}
		while (diff > 0){  //  this is bigger
			secondClone.coefficients.add(0);
			diff--;
		}
			
		//   Now 'firstClone' and 'secondClone' have the same size.  We add the coefficients
		//   term by term.    If the last coefficient yields a carry, then we add \
		//   more term with the carry.
		
		int tmp;
		int carry = 0;
		
		for (int i=0; i < firstClone.coefficients.size(); i++  ){
			tmp = firstClone.coefficients.get(i) + secondClone.coefficients.get(i) + carry;
			sum.coefficients.addLast( tmp % base ) ;
			carry = tmp / base; 
		}
		if (carry > 0)
			sum.coefficients.addLast(carry);
		
		//  ---------  END SOLUTION (add)  ----------
		
		return sum;		
	}
	
	/*
	 *   The subtract method computes a.subtract(b) where a>b.
	 *   If a<b, then it throws an exception.
	 */
	
	public NaturalNumber  subtract(NaturalNumber second) throws Exception{

		//  initialize difference as an empty list of coefficients
		
		NaturalNumber  difference = new NaturalNumber(this.base);

		//   The subtract method shouldn't affect the number itself. 
		//   But the grade school algorithm sometimes requires us to "borrow" 
		//   from a higher coefficient to a lower one.   So let's just work
		//   with a copy (a clone) of 'this' so that we don't modify 'this'.   		

		NaturalNumber  first = this.clone();
		if (this.compareTo(second) < 0){
			System.out.println("Error:  subtract a-b requires that a > b");
			throw new Exception();
		}

		//   ADD YOUR CODE HERE
		
		//  ---------  BEGIN SOLUTION (subtract)  ----------
		
		int i = 0;   //  coefficient position 
		int diffCoef;    //  compute the difference of two coefficients   this[i] - second[i]
		while (i < first.coefficients.size()){
		
			//  Check if 'second' has a term at this position.  
			//  If yes, then need to take the difference at this position.
			//  But if not, then the coefficient is treated as 0 and so 
			//  the diffCoef is 0 so just copy the i'th coefficient 

			if (i < second.coefficients.size()){
				
				diffCoef = first.coefficients.get(i).intValue() - second.coefficients.get(i).intValue();
				if (diffCoef < 0){
					//  .. then we need to borrow from the next coefficient.
					//  But if the next coefficient holds a '0', then we need a sequence of borrows.
					int j = i;
					while (first.coefficients.get(j+1) == 0){
						first.coefficients.set(j+1, base-1);
						j++;
					}
					first.coefficients.set(j+1, new Integer( first.coefficients.get(j+1) - 1 ));
					difference.coefficients.addLast(new Integer(base + diffCoef));
				}
				else
					difference.coefficients.addLast(new Integer(diffCoef));
			}
			else
				difference.coefficients.addLast(new Integer(first.coefficients.get(i)));
			i++;
		}

		//  In the case of say  100-98, we will end up with 002.  
		//  So remove all the leading 0's of the result.

		while ((difference.coefficients.size() > 1) & 
				(difference.coefficients.getLast().intValue() == 0)){
			difference.coefficients.removeLast();
		}
		
		//  ---------  END SOLUTION (subtract)  ----------
		

		return difference;	
	}

	// -------- BEGIN SOLUTION     *helper method* for multiplication  -----
	
	private NaturalNumber multiplyBySingle( int  single){

		//  Assumes that 0 <= single < base. 
		
		int carry = 0;
		int tmp;

		//  initialize prod as an empty list of coefficients
	
		NaturalNumber prod = new NaturalNumber( this.base );
		for (int i=0; i < this.coefficients.size(); i++  ){
			tmp = this.coefficients.get(i) * single + carry;
			prod.coefficients.addLast( tmp % base ) ;
			carry = tmp / base; 
		}
		if (carry > 0)
			prod.coefficients.addLast(carry);
		return prod;
	}
	
	//   END SOLUTION ----------  *helper method* for multiplication ---------
	
	
	
	//   The multiply method should NOT be the same as what you learned in
	//   grade school since that method requires space proportional to the
	//   square of the number of coefficients in the number.   Instead,
	//   you must write a method that uses space that is proportional to
	//   the number of coefficients.    This can be done by basically 
	//   changing the order of loops, as was sketched in class.  
	//
	//  You are not allowed to simply perform addition repeatedly. 
	//  Such a method would be correct, but way too slow to be useful.

	public NaturalNumber multiply( NaturalNumber  second) throws Exception{
		
		//  initialize product as an empty list of coefficients
		
		NaturalNumber product	= new NaturalNumber( this.base );
		
		//    ADD YOUR CODE HERE
		
		// --------------  BEGIN SOLUTION (multiply)  ------------------
		
		if (this.base != second.base){
			System.out.println("ERROR: bases must be the same in a multiplication");
			throw new Exception();
		}
		//  'this' is multiplier,  'second' is multiplicand

		for (int i=0; i < this.coefficients.size(); i++){
			product = product.add(  second.multiplyBySingle( this.coefficients.get(i) ).multiplyByBaseToThePower(i) );
		}		
		
		// remove leading 0's if there are any
		
		while ((product.coefficients.size() > 1) & 
				(product.coefficients.getLast().intValue() == 0)){
			product.coefficients.removeLast();
		}

		
		//  ---------------  END SOLUTION  (multiply) -------------------
		
		return product;
	}
	
	
	//  The divide method divides 'this' by 'second' i.e. this/second.   
	//  'this' is the "dividend", 'second' is the "divisor".
	//  This method ignores the remainder.    
	//
	//  You are not allowed to simply subtract the divisor repeatedly.
	//  This would give the correct result, but it is way too slow!
	
	public NaturalNumber divide( NaturalNumber  divisor ) throws Exception{
		
		//  initialize quotient as an empty list of coefficients
		
		NaturalNumber  quotient = new NaturalNumber(this.base);
		
		//   ADD YOUR CODE HERE.
		
		//  --------------- BEGIN SOLUTION (divide) --------------------------
		
		NaturalNumber  remainder = this.clone();
		
		int numshift = 0;
		NaturalNumber  moreQuotient;
		
		//  The idea of this solution is to repeat the following:
		//     Multiply the divisor by the biggest power of the base, such that the product is less than the 
		//     current remainder.   Then find the coefficient that corresponds to that power.  The latter 
		//     is done by brute force, namely trying the possible coefficients from  0 to base-1.   
		//     You want to find the largest coefficient such that when you multiply the shifted divisor by 
		//     coefficient, the result is less than or equal to the remainder.
		//     (This is essentially the algorithm you learned in grade school!)
		
		while (remainder.compareTo(divisor) >= 0){
			numshift = 0;
			NaturalNumber shiftdivisor = divisor.clone();

			//   shift divisor repeatedly (equivalent to by multiplying by base)
			//   as much as possible such that the shifted divisor is less
			//   than or equal to the remainder
			
			while (remainder.compareTo(shiftdivisor) >= 0){   
				shiftdivisor.coefficients.addFirst(  new Integer(0) );
				numshift++;
			}
			
			//  Notice that we exit the loop when we've shifted too far.
			//  So we need to undo the last shift.  (Not so elegant, but it works.)
			
			shiftdivisor.coefficients.removeFirst();
			numshift--;
			
			//   Divide the shifted divisor into the remainder.   
			//   Since we don't know our times table for arbitrary divisors, 
			//   we do this by iteratively checking (brute force).
			
			int ct = 0;
			while (shiftdivisor.multiplyBySingle(ct+1).compareTo(remainder) <= 0){
				ct++;
			}
			
			//  Now subtract to get a new remainder.
			remainder = remainder.subtract( shiftdivisor.multiplyBySingle(ct) ); 
			
			//  Shift the value of count and add it to the quotient
			
			moreQuotient = new NaturalNumber(this.base, ct);		
			quotient = quotient.add( moreQuotient.multiplyByBaseToThePower(numshift) );
			
		}
		
		// -------------  END SOLUTION  (divide)  ---------------------

		return quotient;		
	}

	/*
	 * The methods should not alter the two numbers.  If a method require
	 * that one of the numbers be altered (e.g. borrowing in subtraction)
	 * then you need to clone the number and work with the cloned number 
	 * instead of the original. 
	 */
	
	public NaturalNumber  clone(){

		//  For technical reasons we'll discuss later, this methods 
		//  has to be declared public (not private).
		//  This detail need not concern you now.

		NaturalNumber copy = new NaturalNumber(this.base);
		for (int i=0; i < this.coefficients.size(); i++){
			copy.coefficients.addLast( new Integer( this.coefficients.get(i) ) );
		}
		return copy;
	}
	
	/*
	 *  The subtraction method computes a-b and requires that a>b.   
	 *  The a.compareTo(b) method is useful for checking this condition. 
	 *  It returns -1 if a < b,  it returns 0 if a == b,  
	 *  and it returns 1 if a > b.
	 */
	
	private int 	compareTo(NaturalNumber second){
		
		int diff = this.coefficients.size() - second.coefficients.size();
		if (diff < 0)
			return -1;
		else if (diff > 0)
			return 1;
		else { 
			boolean done = false;
			int i = this.coefficients.size() - 1;
			while (i >=0 && !done){
				diff = this.coefficients.get(i) - second.coefficients.get(i); 
				if (diff < 0){
					return -1;
				}
				else if (diff > 0)
					return 1;
				else{
					i--;
				}
			}
			return 0;
		}
	}

	/*  computes  a*base^n  where a is the number represented by 'this'
	 */
	
	private NaturalNumber multiplyByBaseToThePower(int n){
		for (int i=0; i< n; i++){
			this.coefficients.addFirst(new Integer(0));
		}
		return this;
	}

	//   This method is invoked by System.out.print.
	//   It returns the string with coefficients in the reverse order 
	//   which is the natural format for people to reading numbers,
	//   i.e..  [ a[N-1], ... a[2], a[1], a[0] ] as in the Tester class. 
	//   It does so simply by make a copy of the list with elements in 
	//   reversed order, and then printing the list using the LinkedList's
	//   toString() method.
	
	public String toString(){	
		LinkedList<Integer> reverseCoefficients = new LinkedList<Integer>();
		for (int i=0;  i < coefficients.size(); i++)
			reverseCoefficients.addFirst( coefficients.get(i));
		return reverseCoefficients.toString();		
	}

}

