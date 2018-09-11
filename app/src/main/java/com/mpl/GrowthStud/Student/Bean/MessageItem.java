package com.mpl.GrowthStud.Student.Bean;

/*
"id": 36,
			"title": "有新的评审待处理",
			"content": "您的孩子司马炅提交了一份我文本，正等待您进行评审，请进入任务栏目给Ta评价吧！",
			"type": 2, //1.系统 2.成就
			"is_read": 0,// 0.未读 1.已读
			"created_at": "2018-09-10"
 */
public class MessageItem {
    private String id, title, content, created_at;
    private int type;
    private int is_read;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }


    public MessageItem(String id, String title, String content, String created_at, int type, int is_read) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.type = type;
        this.is_read = is_read;
    }


}
