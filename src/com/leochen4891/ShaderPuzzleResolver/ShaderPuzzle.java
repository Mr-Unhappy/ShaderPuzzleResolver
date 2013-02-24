package com.leochen4891.ShaderPuzzleResolver;

public class ShaderPuzzle {
	/* A 5*5 puzzle
	    2,1  4   4   1   3  <--vertical rule
      1[hv][h ][h ][h ][h ]
    3,1[ v][  ][  ][  ][  ]
    3,1[ v][  ][  ][  ][  ]
      3[ v][  ][  ][  ][  ]
      3[ v][  ][  ][  ][  ]
      ^
   hor rule
  */
	char[][] mPuzzle;
	String[] mVerRules;
	String[] mHorRules;
	int mX;
	int mY;
	
	// each [  ] has several status
	static public final char STATUS_EMPTY= 'E';
	static public final char STATUS_TRY_YES = 'y';
	static public final char STATUS_TRY_NO = 'n';
	static public final char STATUS_YES = 'Y';
	static public final char STATUS_NO= 'N';
	
	// rules are inputed as "2.1-4-4-1-3" and "1-3.1-3.1-3-3"
	static public final char SEP_NUMBER = '.'; 
	static public final char SEP_LINE = '-';
	
	// and translated into more specific rules array
	// "2.1-4-4-1-3" becomes "*2+1*", "*4*, "*4*", "*1*", "*3*"
	static public final char SEP_NOS = '*'; //0 or more NOs
	static public final char SEP_NOS_PLUS = '+'; //1 or more NOs
	
	static public final int MAX_HOR_LINES = 9;
	static public final int MAX_VER_LINES = 9;
	
	//description string should be like "3.1-1.2-2" for "3,1   1,2   2"
	public ShaderPuzzle(String horRule, String verRule) {
		mHorRules = horRule.split("\\"+SEP_LINE, MAX_HOR_LINES);
		mVerRules = verRule.split("\\"+SEP_LINE, MAX_VER_LINES);
		
		mY = mHorRules.length;
		mX = mVerRules.length;
		
		mPuzzle = new char[mY][mX];
		for (int y = 0; y < mY; y++) {
			for (int x = 0; x < mX; x++) {
				mPuzzle[y][x] = STATUS_EMPTY;
			}
		}
	}
	
	// Check if rule matches line
	// rule looks like: "*3+1*"
	// line looks like: "YYENY"
	// "*1+3+2*" is regex "[EN]*[EY]{1}[EN]+[EY]{3}[EN]+[EY]{2}[EN]*"
	// matches "YNYYYNYY" and "YNYYEEEE", but not "YNNYEEEE"
	public boolean FindRuleInLine(String rule, String line) {
		// construct regex string
		char[] ruleBytes = rule.toCharArray();
		String regex = "";
		for (int i = 0; i < ruleBytes.length; i++) {
			char cur = ruleBytes[i];
			if (SEP_NOS.to == cur) {
				
			}
		}
		
		String regex = "[ENn]*";
		for (int i = 0; i < strRules.length; i++) {
			regex += "[EYy]{" + strRules[i] + "}";
			if (i == strRules.length - 1) { // last Y segment, ends with [EN]*
				regex += "[ENn]*";
			} else { // Y segments sep, ends with [EN]+
				regex += "[ENn]+";
			}
		}
		
		boolean matches = line.matches(regex);
		return matches;
	}
		
	
	/* 
	 2,1  4   4   1   3
  1[hv][h ][h ][h ][h ]
3,1[ v][  ][  ][  ][  ]
3,1[ v][  ][  ][  ][  ]
  3[ v][  ][  ][  ][  ]
  3[ v][  ][  ][  ][  ]
  */
	// check vertical line and horizontal line if the index is not negative
	public boolean CheckLine(int verLine, int horIndex) {
		boolean ret = false;
		if (isHorizontalLine) {
			// NOTE: horizontal line use rules on left, which is mYRules
			String line = "";
			for (int i = 0; i < mXRules.length; i++) {
				line += mPuzzle[index][i];
			}
			ret = FindRuleInLine(mYRules[index], line);
		} else {
			// vertical line need to be constructed now
			String line = "";
			for (int i = 0; i < mYRules.length; i++) {
				line += mPuzzle[i][index];
			}
			ret = FindRuleInLine(mXRules[index], line);
		}
			
		return ret;
	}
	
	public boolean CheckVerticalLine(int index) {
		
	}
	
	public boolean Check () {
		boolean result = true;
		// horizontal lines
		for (int i = 0; i < mYRules.length; i++) {
			result = CheckLine(true, i);
			if (false == result) return false;
		}
		
		// vertical lines
		for (int i = 0; i < mXRules.length; i++) {
			result = CheckLine(false, i);
			if (false == result) return false;
		}
		return result;
	}
	
	public boolean SetLine(boolean isHorizontal, int index, char[] line) {
		if (isHorizontal) {
			if (line.length != mXRules.length) 
				return false;
			for (int i = 0; i < line.length; i++) {
				mPuzzle[index][i] = line[i];
			}
		} else { // vertical
			if (line.length != mYRules.length) 
				return false;
			for (int i = 0; i < line.length; i++) {
				mPuzzle[i][index] = line[i];
			}
		}
		return true;
	}
	
	public char Get(int x, int y) {
		return mPuzzle[y][x];
	}
	
	public boolean Set(int x, int y, char value) {
		if (x < 0 || x > mXRules.length || y < 0 || y > mYRules.length)
			return false;
		mPuzzle[y][x] = value;
		return true;
	}
	
	public void CleanTry() {
		for (int y = 0; y < mYRules.length; y++) {
			for (int x = 0; x < mXRules.length; x++) {
				if (STATUS_TRY_YES == mPuzzle[y][x] || STATUS_TRY_NO == mPuzzle[y][x])
					mPuzzle[y][x] = STATUS_EMPTY;
			}
		}
	}
	
	public void ConfirmTry() {
		for (int y = 0; y < mYRules.length; y++) {
			for (int x = 0; x < mXRules.length; x++) {
				if (STATUS_TRY_YES == mPuzzle[y][x])
					mPuzzle[y][x] = STATUS_YES;
				if (STATUS_TRY_NO == mPuzzle[y][x])
					mPuzzle[y][x] = STATUS_NO;
			}
		}
	}
}
