package librarysystem;

public enum MenuType {
	HOME("DashBoard"), CHECKOUT("Issue Book"), OVERDUE("Over Due Report"), BOOK("Book Info"), MEMBER("Member Info");
	
	private final String text;
	
	private MenuType(String name) {
        this.text = name;
    }

    public String getText() {
        return text;
    }
    
    public static MenuType getMenuType(String text){
        for(MenuType mt : MenuType.values()){
            if(mt.text.equals(text)) return mt;
        }
        return null;
    }
}
