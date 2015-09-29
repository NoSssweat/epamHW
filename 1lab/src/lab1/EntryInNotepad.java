package lab1;

public class EntryInNotepad {
	public static void main(String[] args){
		Notepad notepad = new Notepad();
		
		notepad.addEntry("Ночь, улица, фонарь, аптека");
		notepad.addEntry("Бессмысленный и тусклый свет");
		notepad.addEntry("Живи еще хоть четверть века");
		notepad.addEntry("Все будет так. Исхода нет");
		notepad.showAll();
		
		notepad.editEntry(3, "Измененная строка.");
		notepad.showAll();
		
		notepad.deleteEntry(3);
		notepad.showAll();
		
		notepad.addEntry("Живи еще хоть четверть века");
		notepad.addEntry("Умрешь - начнешь опять сначала");
		notepad.addEntry("И повторится все, как встарь:");
		notepad.addEntry("Ночь, ледяная рябь канала,");
		notepad.addEntry("Аптека, улица, фонарь.");
		notepad.addEntry("");
		notepad.addEntry("Александр Блок");
		notepad.showAll();
		
		
	}
}
