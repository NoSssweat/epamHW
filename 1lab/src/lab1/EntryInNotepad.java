package lab1;

public class EntryInNotepad {
	public static void main(String[] args){
		Notepad notepad = new Notepad();
		
		notepad.addEntry("����, �����, ������, ������");
		notepad.addEntry("������������� � ������� ����");
		notepad.addEntry("���� ��� ���� �������� ����");
		notepad.addEntry("��� ����� ���. ������ ���");
		notepad.showAll();
		
		notepad.editEntry(3, "���������� ������.");
		notepad.showAll();
		
		notepad.deleteEntry(3);
		notepad.showAll();
		
		notepad.addEntry("���� ��� ���� �������� ����");
		notepad.addEntry("������ - ������� ����� �������");
		notepad.addEntry("� ���������� ���, ��� ������:");
		notepad.addEntry("����, ������� ���� ������,");
		notepad.addEntry("������, �����, ������.");
		notepad.addEntry("");
		notepad.addEntry("��������� ����");
		notepad.showAll();
		
		
	}
}
