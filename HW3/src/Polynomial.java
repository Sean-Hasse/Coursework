/* Sean Hasse
 * HW 3
 * 11/11/2018
 */

import java.util.*;
import java.lang.*;

// TermNode class will be used to make the LinkedList in Polynomial
public class Polynomial
{
    private static class TermNode
    {
        private double coefficient;
        private int exponent;
        private TermNode next;

        public TermNode(int exp, double coeff, TermNode nextTerm )
        {
            coefficient= coeff;
            exponent = exp;
            next = nextTerm;
        }

        // returns the coefficient variable
        public double getCoefficient()
        {
            return coefficient;
        }

        public int getExponent()
        {
            // returns the exponent variable
            return exponent;
        }

        // returns the next variable
        public TermNode getNext()
        {
            return next;
        }

        // sets the coefficient variable to input value
        public void setCoefficient(double newCoeff)
        {
            coefficient = newCoeff;
        }

        // sets the exponent variable to input value
        public int setExponent(int newExp)
        {
            exponent = newExp;
            return exponent;
        }

        // sets the next variable to input value
        public void setNext(TermNode newNext)
        {
            next = newNext;
        }

        // addAfter adds a new TermNode after this node
        public void addAfter(int exp, double coef)
        {
            next = new TermNode(exp, coef, next);
        }

        // removeAfter removes the TermNode after this node
        public void removeAfter()
        {
            next = next.next;
        }

        // listLength returns the length of the LinkedList
        public static int listLength(TermNode head)
        {
            TermNode cursor;
            int answer;

            answer = 0;
            for (cursor = head; cursor != null; cursor = cursor.next)
                answer++;

            return answer;
        }

        // copy returns a copy of the source LinkedList
        public static TermNode copy(TermNode source)
        {
            TermNode copyHead;
            TermNode cursor;

            if (source == null)
                return null;

            copyHead = new TermNode(source.exponent, source.coefficient, null);
            cursor = copyHead;

            while(source.next != null)
            {
                source = source.next;
                cursor.addAfter(source.exponent,source.coefficient);
                cursor = cursor.next;
            }

            return copyHead;
        }
    }
    // instance variables of Polynomial
    // first is the term of the Polynomial with the highest degree
    // except the constant term, only non-zero terms are stored.
    private TermNode first;


    /**Postcondition:  Creates a polynomial which is 0.
     * **/
    public Polynomial()
    {
        first = new TermNode(0,0, null);
    }

    /**Postcondition:  Creates a polynomial which has a single term a0*x^0
     * @param a0 The value to be set as the coefficient of the constant (x^0) term.
     * **/
    public Polynomial(double a0)
    {
        first = new TermNode(0,a0,null);
    }

    /** Postcondition:  Creates a copy of Polynomial p
     * @param p the Polynomial which is to be copied.
     * **/
    public Polynomial(Polynomial p)
    {
        first = TermNode.copy(p.first);
    }

    /** Postcondition:  Adds the given amount to the coefficient of the specified exponent.
     * @param amount The amount to be added to the coefficient.
     * @param exponent The degree of the term whose coefficient is to be modified.
     * (1) Note that the exponent can be arbitrary
     * (2) If you want, you can assume the amount is not 0, however, it is possible that
     *   after you add the amount, the coefficient becomes 0, in which case, you should delete the TermNode
     * **/
    public void add_to_coef(double amount, int exponent)
    {
        // make a cursor, and incriment it until cursor.next.exponent <= input exponent
        TermNode cursor = first;
        while(cursor.getNext() != null && (cursor.getNext().getExponent() >= exponent))
        {
            cursor = cursor.getNext();
        }

        // if a TermNode with the same exponent as input exists, add the two coefficients and check for 0
        if(cursor.getExponent() == exponent)
        {
            cursor.setCoefficient(cursor.getCoefficient() + amount);
        }
        // else if no TermNode with the same exponent exists, create a new one
        else
        {
            cursor.addAfter(exponent, amount);
        }
        return;
    }

    /** Postcondition:  Sets the coefficient of a specified term to a specified value.
     * @param coefficient The new value of the coefficient.
     * @param exponent The degree of the term whose coefficient is to be modified.
     * (1) Note that the exponent can be arbitrary
     * (2) The coefficient may be 0
     * **/
    public void assign_coef(double coefficient, int exponent)
    {
        // create cursor and increment until cursor.next.exponent is not larger than input exponent
        TermNode cursor = first;
        while(cursor.getNext() != null && cursor.getNext().getExponent() > exponent)
        {
            cursor = cursor.getNext();
        }

        // check if cursor.next == null
        if(cursor.getNext() == null)
        {
            if(exponent >=0 && coefficient !=0)
                cursor.addAfter(exponent, coefficient);
        }
        // then check if the input exponent exists already or not
        else if(cursor.getNext().getExponent() == exponent)
        {
            // check if coefficient is zero. If it is, delete the next node instead
            if(coefficient == 0)
                cursor.removeAfter();
            else
                cursor.getNext().setCoefficient(coefficient);
        }
        // lastly, if cursor is not the last element and input exponent does not already exist, create a new Term Node after cursor
        else if(coefficient != 0)
            cursor.addAfter(exponent, coefficient);
    }

    /**  Postcondition:   Returns coefficient at specified exponent of this polynomial.
     * @param exponent The exponent of the term whose coefficient is sought.
     * @return The coefficient of the term.
     * @throws Exception if the degree of the activating polynomial is less than that of the requested term.
     * **/
    public double coefficient(int exponent)
    {
        TermNode cursor = first;
        for(cursor = first; cursor.getNext() != null; cursor.getNext())
        {
            // step through each node, and check if the exponent is equal to target
            if(cursor.getExponent() == exponent)
                return cursor.getCoefficient();
        }

        // check the last item in the linked list
        if(cursor.getExponent() == exponent)
            return cursor.getCoefficient();
        else
            return 0;
    }

    /** @return The value of this Polynomial with the given value for the variable x.
     * @param x The value at which the Polynomial is to be evaluated.
     * using Horner's method to evaluation
     * see the link here
     * https://en.wikipedia.org/wiki/Horner%27s_method
     *
     ***/
    public double eval(double x)
    {
        TermNode t = first;
        double sum = t.getCoefficient() * x;

        int curExponent = t.getExponent() - 1;

        // use the Horner's Rule as long as it fits (ie, exponents are sequential)
        if (t.getNext() != null && (t.getExponent() - 1) == t.getNext().getExponent()) {
            sum = sum + t.getNext().getCoefficient();
        }

        // otherwise, deal on a case-by-case scenario, stepping through each using curExponent and t
        while (curExponent > 0) {
            if (t.getNext() == null) {
                sum = sum * x;
                curExponent = curExponent - 1;
            } else if (t.getNext() != null && (t.getExponent() - 1) > t.getNext().getExponent()) {
                sum = sum * x;
                curExponent = curExponent - 1;
            } else {
                sum = (sum * x) + t.getNext().getCoefficient();
                curExponent = curExponent - 1;
            }
        }
        // return when calculations are complete
        return sum;
    }



        /**@return Returns a string representing the polynomial expression with coefficients displayed to the tenths place,
         * omitting any coefficients that are zero.
         * If all coefficients are 0, then the zero function is reported.
         *
         **/
    public String toString()
    {
        // check first for zero function
        if(TermNode.listLength(first) == 1 && first.getExponent() == 0)
            return "0";
        else
        {
            // go through each TermNode and add it to a full string
            // go through each TermNode and add it to a full string
            String str = "";

            for(TermNode cursor = first; cursor.getNext() != null; cursor.getNext())
            {
                // add to str based on formatting
                if(cursor.getCoefficient() > 0)
                    str += ( " + " + cursor.getCoefficient() + "x^" + cursor.getExponent());
                else
                    str += (cursor.getCoefficient() + "x^" + cursor.getExponent());
            }

            // when done building the string, return
            return str;
        }
    }

    /**@return Returns a Polynomial that is the sum of p and this Polynomial.
     * @param p The Polynomial to be added to the activating Polynomial.
     * **/
    public Polynomial add(Polynomial p)
    {
        // create two cursors and return polynomial
        TermNode cursor1 = this.first;
        TermNode cursor2 = p.first;
        Polynomial sum = new Polynomial();

        // while there are more terms in either polynomial, step through each and add them to the sum polynomial
        while (cursor1.getNext() != null || cursor2.getNext() != null)
        {
            if (cursor1.getExponent() > cursor2.getExponent() && cursor1.getNext() != null)
            {
                sum.assign_coef(cursor1.getCoefficient(), cursor1.getExponent());
                cursor1 = cursor1.getNext();
            }
            else if (cursor2.getExponent() > cursor1.getExponent() && cursor2.getNext() != null)
            {
                sum.assign_coef(cursor2.getCoefficient(), cursor2.getExponent());
                cursor2 = cursor2.getNext();
            }
            else if (cursor1.getExponent() == cursor2.getExponent() && (cursor1.getNext() != null && cursor2.getNext()!= null))
            {
                sum.add_to_coef((cursor1.getCoefficient() + cursor2.getCoefficient()), cursor1.getExponent());
                cursor1 = cursor1.getNext();
                cursor2 = cursor2.getNext();
            }

            // cover cases where one of them is the last node
            else if(cursor1.getNext() == null)
            {
                sum.add_to_coef(cursor2.getCoefficient(), cursor2.getExponent());
                cursor2 = cursor2.getNext();
            }
            else if(cursor2.getNext() == null)
            {
                sum.add_to_coef(cursor1.getCoefficient(), cursor1.getExponent());
                cursor1 = cursor1.getNext();
            }
        }

        // out of the while loop, both cursors are at their last items
        if (cursor1.getExponent() > cursor2.getExponent() && cursor1.getNext() != null)
        {
            sum.assign_coef(cursor1.getCoefficient(), cursor1.getExponent());
        }
        else if (cursor2.getExponent() > cursor1.getExponent() && cursor2.getNext() != null)
        {
            sum.assign_coef(cursor2.getCoefficient(), cursor2.getExponent());
        }
        else if (cursor1.getExponent() == cursor2.getExponent() && (cursor1.getNext() != null && cursor2.getNext()!= null))
        {
            sum.add_to_coef((cursor1.getCoefficient() + cursor2.getCoefficient()), cursor1.getExponent());
        }
        return sum;
    }




    /** Postcondition:  Returns a new polynomial obtained by multiplying this term and p. For example, if this polynomial is
     2x^2 + 3x + 4 and p is 5x^2 - 1x + 7, then at the end of this function, it will return the polynomial 10x^4 + 13x^3 + 31x^2 + 17x + 28.
     @param p The polynomial to be multiplied.
     @return The product of the activating Polynomial and p.
     **/
    public Polynomial multiply(Polynomial p)
    {
        // create the return polynomial and cursors
        Polynomial product = new Polynomial();
        TermNode cursor1 = this.first;
        TermNode cursor2 = p.first;

        for(cursor1 = this.first; cursor1 != null; cursor1.getNext())
        {
            for(cursor2 = p.first; cursor2 != null; cursor2.getNext())
            {
                product.add_to_coef(cursor1.getCoefficient() * cursor2.getCoefficient(), cursor1.getExponent() + cursor2.getExponent());
            }
        }
        return product;
    }
}