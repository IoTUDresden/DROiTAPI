package eu.vicci.nao.driver.example;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import eu.vicci.driver.nao.Nao;

public class GUI extends JFrame implements KeyListener {
	private Nao nao;
	private boolean walking = false;
	private static final long serialVersionUID = -954439051015909440L;
	private static GUI gui = null;

	public static GUI getInstance() {
		if (gui == null)
			gui = new GUI();
		return gui;
	}

	private GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(this);
		setTitle("Nao Remote Control");
		setSize(300, 100);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		switch (key) {
		case KeyEvent.VK_UP:
			nao.doMoveForward();
			walking = true;
			System.out.println("Laufe...");
			nao.setEyeColor("green");
			break;
		case KeyEvent.VK_DOWN:
			nao.stopMoving();
			walking = false;
			System.out.println("Stoppe... ");
			nao.setEyeColor("red");
			break;
		case KeyEvent.VK_LEFT:
			 nao.getEngine().doSteerLeft();
			 nao.setEyeColor("green");
			break;
		case KeyEvent.VK_RIGHT:
			 nao.getEngine().doSteerRight();
			 nao.setEyeColor("green");
			break;
		case KeyEvent.VK_W:
			nao.doMoveForward();
			walking = true;
			System.out.println("Laufe forwärts...");
			nao.setEyeColor("green");
			break;
		case KeyEvent.VK_A:
			 nao.doMoveLeft();
			 nao.setEyeColor("green");
			break;
		case KeyEvent.VK_S:
			nao.doMoveBackward();
			nao.setEyeColor("green");
			System.out.println("Laufe rückwärts...");
			break;
		case KeyEvent.VK_D:
			 nao.doMoveRight();
			 nao.setEyeColor("green");
			break;
		case KeyEvent.VK_R:
			nao.setEyeColor("red");
			break;
		case KeyEvent.VK_G:
			nao.setEyeColor("green");
			break;
		case KeyEvent.VK_B:
			nao.setEyeColor("blue");
			break;
		case KeyEvent.VK_H:
			nao.sayText("Nao");
			break;
		case KeyEvent.VK_L:
			nao.speech();
			break;
		case KeyEvent.VK_ENTER:
			// nao.sendServiceCall("/rosout/get_loggers");
			break;
		default:
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		// case KeyEvent.VK_UP:
		// walking = false;
		// nao.stopMoving();
		// break;
		// case KeyEvent.VK_DOWN:
		// nao.stopMoving();
		// break;
		// case KeyEvent.VK_LEFT:
		// nao.stopMoving();
		// break;
		// case KeyEvent.VK_RIGHT:
		// nao.stopMoving();
		// break;
		default:
		}
	}

	public Nao getNao() {
		return nao;
	}

	public void setNao(Nao nao) {
		this.nao = nao;
	}

}