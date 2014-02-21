package rpg.test;

import static org.junit.Assert.*;

import org.junit.Test;

import rpg.game.LocalMap;
import rpg.game.LocalPosition;
import rpg.tcp.GridPrinter;

public class GridTest {
	@Test
	public void origin() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(0, 0), new LocalMap(2, 2));
		assertEquals(
				"|       |       |\n"+
				"|  you  |       |\n",
			g.print("you"));
	}
	
	@Test
	public void origin2x5() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(0, 0), new LocalMap(5, 2));
		assertEquals(
				"|       |       |       |       |       |\n"+
				"|  you  |       |       |       |       |\n",
			g.print("you"));
	}
	
	@Test
	public void upOneTile() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(0, 1), new LocalMap(2, 2));
		assertEquals(
			"|  you  |       |\n"+
			"|       |       |\n",
			g.print("you"));
	}
	
	@Test
	public void upOneTile2x5() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(0, 1), new LocalMap(5, 2));
		assertEquals(
				"|  you  |       |       |       |       |\n"+
				"|       |       |       |       |       |\n",
			g.print("you"));
	}
	
	@Test
	public void topRight() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(1, 1), new LocalMap(2, 2));
		assertEquals(
			"|       |  you  |\n"+
			"|       |       |\n",
			g.print("you"));
	}
	
	@Test
	public void bottomRight() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(1, 0), new LocalMap(2, 2));
		assertEquals(
			"|       |       |\n"+
			"|       |  you  |\n",
			g.print("you"));
	}
	
	@Test
	public void middleUp2x5() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(2, 1), new LocalMap(5, 2));
		assertEquals(
				"|       |       |  you  |       |       |\n"+
				"|       |       |       |       |       |\n",
			g.print("you"));
	}
	
	@Test
	public void bottomRight2x5() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(4, 0), new LocalMap(5, 2));
		assertEquals(
				"|       |       |       |       |       |\n"+
				"|       |       |       |       |  you  |\n",
			g.print("you"));
	}
	
	@Test
	public void namesAreTrimmed() throws Exception {
		GridPrinter g = new GridPrinter(new LocalPosition(0, 0), new LocalMap(2, 2));
		assertEquals(
				"|       |       |\n"+
				"|  Joh  |       |\n",
			g.print("John"));
	}
}
