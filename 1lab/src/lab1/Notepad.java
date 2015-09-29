package lab1;



public class Notepad {
	
	String[][] array = new String[2][500];
	int lastIndex =0;
	
	
	public void addEntry(String text){
		
		// Ищем не заполненый эллемент, если индекс этого эллемента больше или равен индексу последнего пишем в него, 
		//индекс инкриментируем. Иначе просто записываем в индекс.
		for (int i=0; i< array[0].length; i++){
			
			if ((array[0][i]==null)&&(i<lastIndex)){
				array[0][i] = Integer.toString(lastIndex);
				array[1][i] = text;
				break;
			
			}
			else if ((array[0][i]==null)&&(i>=lastIndex)){
				array[0][i] = Integer.toString(lastIndex);
				array[1][i] = text;
				lastIndex++;
				break;
			}		
		}
	}
	
	//Удаление строки
	public void deleteEntry(int deleteId){
		array[0][deleteId-1] = null;
		array[1][deleteId-1] = null;
	}
	
	//Редактирование строки
	public void editEntry(int editId, String text){
		array[1][editId-1] = Integer.toString(editId);
		array[1][editId-1] = text;
	}
	
	//Вывод массива заполненых строк
	public void showAll(){
		System.out.println("Id:| Текст:");
		for (int i=0; i<lastIndex; i++){
			System.out.printf("%3d", i+1);
			System.out.println("|  "+array[1][i]);			
		}
		System.out.println();
	}	
}
