package fanhao

import com.fast.dev.core.util.text.TextUtil
import groovy.util.logging.Log
import org.apache.commons.io.FileUtils
import org.springframework.util.StringUtils

class ConverFanHao {

    private static final File path = new File("E:/git/github/fanhaodaquan");


    public static void main(String[] args) {

        Set<String> rests = new HashSet<>()
        String[] extName = ["txt"];
        Collection<File> files = FileUtils.listFiles(path, extName, true);
        for (File file : files) {
            readText(rests, file);
        }

        println rests
    }

    private static void readText(Set<String> rests, File file) {
        println("read : " + file)

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//构造一个BufferedReader类来读取文件
        String s = null;
        while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
            String wd = TextUtil.subText(s, "【", "】", -1);
            def arr = wd.split("-");
            if (arr.length > 1) {
                String title = arr[0];
                if (StringUtils.hasText(title) && title.length() > 2) {
                    rests.add(title)
                }
            }

        }
        br.close();


    }


}
