import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools {

	public static void main(String[] args) {
		
		//testRemoveBrackets();
	}
	
	private static void testGetSingerNames() {
		getSingerNames("韦礼安 Matzka aaa 甲乙 丙丁 bbb-金银岛-国语-流行歌曲.mkv");		
	}
	
	// 测试改名
	private static void testGetInvalidNames() {
		String[] test1 = {
				"刘德华-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华 关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华^关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华*关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华&关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华-关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华_关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华+关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华vs关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华关芝琳-相约到永久(MTV)-国语-合唱歌曲.mkv",
				"刘德华(蒙面歌王)-相约到永久(MTV)-国语-合唱歌曲.mkv",
		};
		for (String s : test1) {
			String res = getInvalidNames(s);
			if (res == null) {
//				 System.out.println("正确:" + s);
			} else {
//				 System.out.println("更正:" + s + "->" +res);
			}
		}
	}	
	
	// 测试去括弧
	private static void testRemoveBrackets() {
		System.out.println(removeBrackets("相约到永久(MTV)"));
	}

	public static String[] getSingerNames(String fileName) {
		String[] res = null;
		if (fileName.isEmpty()) {
			return null;
		}
		fileName = fileName.trim();
		if (fileName.isEmpty()) {
			return null;
		}

		String pattern = "^([\\S ]+?)(?=-)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(fileName);
		if (m.find()) {
			String singers = m.group(0);
			singers.trim();
			singers = singers.replaceAll(" +", " ");
			// System.out.println("Found value:" + singers);
			res = singers.split(" ");
		} else {
			System.out.println("NO MATCH");
		}
		// for (String s:res) {System.out.println(s);}

		return res;
	}

	public static String getInvalidNames(String filename) {
		String res = null;
		String[] fragments = filename.split("-");
		String singers = null;
		if (fragments.length < 4) {
			return filename;
		}

		if (fragments.length == 4) {
			singers = fragments[0];
		} else {
			StringBuffer tmp = new StringBuffer();
			for (int i = 0; i < fragments.length - 3; i++) {
				if (i != 0) {
					tmp.append("-");
				}
				tmp.append(fragments[i]);
			}
			singers = tmp.toString();
//			 System.out.println("发现多-分割歌手名：" + filename + " 异常部分为" + singers);
		}

		String correct = singers.replaceAll("&|\\*|\\^|_|-|\\+|vs", " ");
		if (!singers.equals(correct)) {
			//res = filename.replaceFirst(singers, correct);
			res = filename.replace(singers, correct);
			filename = res;
//			System.out.println("发现异常歌手名:" + singers + "->" + correct);
		} 
		
		//String tmpFileName = filename;
		
		//检查括弧
		String noBracket = removeBrackets(singers);
		if (!singers.equals(noBracket)) {
			System.out.println("发现括弧歌手名:" + singers + "->" + noBracket);
			res = filename.replace(singers, noBracket);
			filename = res;
			singers = noBracket;
		}
		
		//检查超长
		if (singers.length() > 4) {
			String[] tmp = singers.split(" ");
			for (String s : tmp) {
				if (s.length() > 4) {
//					System.out.println("发现超长歌手名:" + filename);
					res = filename;
					filename = res;
				}
			}
		}

		return res;
	}

	public static String removeBrackets(String input) {
		String res = input;

		Pattern p = Pattern.compile("(\\(\\S*\\))");
		Matcher m = p.matcher(input);
		int i = 0;
		while (m.find()) {
			String brackets = m.group(i++);
			res = res.replace(brackets, "");
		}
		
		return res;
	}
}
