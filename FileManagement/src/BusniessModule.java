import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BusniessModule {
	String MKV = ".mkv";
	String sourcePath = null;
	ArrayList<String> tmpFolders = null;
	ArrayList<FileTransferModule> fileTranList = null;
	ArrayList<FileRenameModule> fileRenameList = null;

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	
	public void preview(MessageDTO msg) {
		System.out.println("----------------Start preview------------------");
		msg.err = null;
		HashMap<String, Integer> singerRank = new HashMap<String, Integer>(); // 候选歌手名单
		try {
			File file = new File(sourcePath);
			if (!file.exists()) {
				msg.err = "文件目录异常";
				return;
			}

			if (!file.isDirectory()) {
				msg.err = "文件目录异常";
				return;
			}

			String[] files = file.list();// 返回该目录下所有文件及文件夹数组
			Arrays.sort(files); // 排序
			fileTranList = new ArrayList<FileTransferModule>();
			tmpFolders = new ArrayList<String>();

			// 第一遍找歌
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(MKV)) {
					FileTransferModule fm = new FileTransferModule();
					fm.fileName = files[i];

					// 根据文件名找 歌手名
					String[] singers = StringTools.getSingerNames(fm.fileName);
					for (String s : singers) {
						if (singerRank.containsKey(s)) {
							Integer count = singerRank.get(s);
							singerRank.put(s, count + 1);
						} else {
							singerRank.put(s, 1);
						}
						fm.singerNames = singers;
					}
					fileTranList.add(fm);
				}
			}

			// 第二遍确定歌手文件夹
			for (int i = 0; i < fileTranList.size(); i++) {
				FileTransferModule fm = fileTranList.get(i);
				String[] singers = fm.singerNames;
				if (singers.length == 1) {
					fm.folderName = singers[0];
				} else {
					fm.folderName = getMainSinger(singerRank, singers);
				}
			}

			msg.setOutput(fileTranList);
		} catch (Exception e) {
			msg.err = e.getMessage();
			return;
		}

		System.out.println("----------------End preview------------------");
		return;
	}

	public void move(MessageDTO msg) {
		System.out.println("----------------Start moving------------------");
		msg.err = null;

		for (int i = 0; i < fileTranList.size(); i++) {
			FileTransferModule fm = fileTranList.get(i);
			String folderName = fm.folderName;
			File folder = new File(sourcePath + "\\" + folderName);
			// 如果文件夹不存在则创建
			if (!folder.exists() && !folder.isDirectory()) {
				folder.mkdir();
			}

			String fromPath = sourcePath + "\\" + fm.fileName;
			String toPath = sourcePath + "\\" + folderName + "\\" + fm.fileName;
			File file = new File(fromPath);
			if (!file.exists() && !file.isDirectory()) {
				fm.status = "找不到源文件！";
			} else {
				//System.out.println("準備移動" + fromPath + "到" + toPath);
				if (file.renameTo(new File(toPath))) {
					fm.status = "成功";
				} else {
					fm.status = "失敗";
				}
			}
			msg.setOutput(fileTranList);
		}

		System.out.println("----------------End moving------------------");
		return;
	}

	public void renamePreview(MessageDTO msg) {
		System.out.println("----------------Start name check------------------");
		msg.err = null;
		try {
			File file = new File(sourcePath);
			if (!file.exists()) {
				msg.err = "文件目录异常";
				return;
			}

			if (!file.isDirectory()) {
				msg.err = "文件目录异常";
				return;
			}

			String[] files = file.list();// 返回该目录下所有文件及文件夹数组
			Arrays.sort(files); // 排序
			fileRenameList = new ArrayList<FileRenameModule>();
			tmpFolders = new ArrayList<String>();

			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(MKV)) {

					// 检查是否有异常，如果有的话就返回。
					String newName = StringTools.getInvalidNames(files[i]);
					if (newName != null) {
						FileRenameModule fm = new FileRenameModule();
						fm.orgName = files[i];
						fm.newName = newName;
						fileRenameList.add(fm);
					}
				}
			}

			msg.setOutput(fileRenameList);
		} catch (Exception e) {
			msg.err = e.getMessage();
			return;
		}

		System.out.println("----------------End name check------------------");
		return;
	}	
	
	public void rename(MessageDTO msg) {
		System.out.println("----------------Start rename------------------");
		msg.err = null;

		for (int i = 0; i < fileRenameList.size(); i++) {
			FileRenameModule fm = fileRenameList.get(i);

			String fromPath = sourcePath + "\\" + fm.orgName;
			String toPath = sourcePath + "\\" + fm.newName;
			File file = new File(fromPath);
			if (!file.exists() && !file.isDirectory()) {
				fm.status = "找不到源文件！";
			} else {
//				System.out.println("准备改名" + fromPath + "到" + toPath);
				if (file.renameTo(new File(toPath))) {
					fm.status = "成功";
				} else {
					fm.status = "失败";
				}
			}
			msg.setOutput(fileTranList);
		}

		System.out.println("----------------End rename------------------");
		return;
	}
	
	private String getMainSinger(HashMap<String, Integer> singerRank, String[] singers) {
		String winer = singers[0];
		for (String s : singers) {
			Integer winCount = singerRank.get(winer);
			Integer curCount = singerRank.get(s);
			if (curCount > winCount) {
				winer = s;
			}
		}

		return winer;
	}
}
