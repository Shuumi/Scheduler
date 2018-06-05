package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import serializers.conflict.ConflictGraph.Node;

public class DrawGraph extends JPanel {
	private HashSet<Node> conflictGraph;
	private int nodeCount = 0;
	private int circleRadius = 25;
	private HashMap<String, Integer> paintedTransactionNodes = new HashMap<>();

	public DrawGraph(HashSet<Node> cg) {
		conflictGraph = cg;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(getGraphics());
		paintGraph(g);
	}

	public void paintGraph(Graphics g) {
		paintNodes(g);
		paintVertices(g);
	}

	public void paintNodes(Graphics g) {
		for (Node n : conflictGraph) {
			if (!paintedTransactionNodes.containsKey(n.getStart())) {
				addNode(g, n.getStart());
				paintedTransactionNodes.put(n.getStart(), nodeCount - 1);
			}
			if (!paintedTransactionNodes.containsKey(n.getEnd())) {
				addNode(g, n.getEnd());
				paintedTransactionNodes.put(n.getEnd(), nodeCount - 1);
			}
		}
	}

	public void paintVertices(Graphics g) {
		for (Node n : conflictGraph) {
			g.setColor(new Color(0, 0, 0));
			int startNodeIndex = paintedTransactionNodes.get(n.getStart());
			int endNodeIndex = paintedTransactionNodes.get(n.getEnd());
			int startx = 150 * (startNodeIndex % 3) + 150 + circleRadius;
			int starty = 150 * (startNodeIndex / 3) + circleRadius;
			int endx = 150 * (endNodeIndex % 3) + 150 + circleRadius;
			int endy = 150 * (endNodeIndex / 3) + circleRadius;
			drawArrow(g, startx, starty, endx, endy, 45, 25);
		}
	}

	public void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int maxArrowAngle, int maxArrowLenght) {
		g.setColor(new Color((int) (Math.random() * 255), (int) (Math.random() * 100), (int) (Math.random() * 100)));
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		int lineoffset = 5;

		int arrowAngle = (int)(Math.random()*(maxArrowAngle-10))+10;
		int arrowLenght = (int)(Math.random()*(maxArrowLenght-10))+10;
		int arrx1, arry1, arrx2, arry2;
		if (deltaX > 0) {
			if (deltaY < 0) {
				double lineAngle = Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

				x1 += (int) (Math.sin(lineAngle) * circleRadius);
				y1 -= (int) (Math.cos(lineAngle) * circleRadius);
				x2 -= (int) (Math.sin(lineAngle) * circleRadius);
				y2 += (int) (Math.cos(lineAngle) * circleRadius);

				lineAngle = Math.toDegrees(lineAngle);

				arrx1 = x2 + (int) (Math.cos(Math.toRadians(270 - lineAngle + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(270 - lineAngle + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(270 - lineAngle - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(270 - lineAngle - arrowAngle)) * arrowLenght);
			} else if (deltaY > 0) {
				double lineAngle = Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

				x1 += (int) (Math.sin(lineAngle) * circleRadius);
				y1 += (int) (Math.cos(lineAngle) * circleRadius);
				x2 -= (int) (Math.sin(lineAngle) * circleRadius);
				y2 -= (int) (Math.cos(lineAngle) * circleRadius);

				lineAngle = Math.toDegrees(lineAngle);

				arrx1 = x2 + (int) (Math.cos(Math.toRadians(90 + lineAngle + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(90 + lineAngle + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(90 + lineAngle - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(90 + lineAngle - arrowAngle)) * arrowLenght);
			} else {
				x1 += circleRadius;
				x2 -= circleRadius;
						
				arrx1 = x2 + (int) (Math.cos(Math.toRadians(180 + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(180 + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(180 - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(180 - arrowAngle)) * arrowLenght);
			}
		} else if (deltaX < 0) {
			if (deltaY < 0) {
				double lineAngle = Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

				x1 -= (int) (Math.sin(lineAngle) * circleRadius);
				y1 -= (int) (Math.cos(lineAngle) * circleRadius);
				x2 += (int) (Math.sin(lineAngle) * circleRadius);
				y2 += (int) (Math.cos(lineAngle) * circleRadius);

				lineAngle = Math.toDegrees(lineAngle);

				arrx1 = x2 + (int) (Math.cos(Math.toRadians(270 + lineAngle + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(270 + lineAngle + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(270 + lineAngle - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(270 + lineAngle - arrowAngle)) * arrowLenght);
			}
			else if (deltaY > 0) {
				double lineAngle = Math.atan(Math.abs(deltaX) / Math.abs(deltaY));

				x1 -= (int) (Math.sin(lineAngle) * circleRadius);
				y1 += (int) (Math.cos(lineAngle) * circleRadius);
				x2 += (int) (Math.sin(lineAngle) * circleRadius);
				y2 -= (int) (Math.cos(lineAngle) * circleRadius);

				lineAngle = Math.toDegrees(lineAngle);

				arrx1 = x2 + (int) (Math.cos(Math.toRadians(90 - lineAngle + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(90 - lineAngle + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(90 - lineAngle - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(90 - lineAngle - arrowAngle)) * arrowLenght);
			} else {
				x1 -= circleRadius;
				x2 += circleRadius;
				
				arrx1 = x2 + (int) (Math.cos(Math.toRadians(arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(-arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(-arrowAngle)) * arrowLenght);
			}
		} else {
			if (deltaY < 0) {
				y1 -= circleRadius;
				y2 += circleRadius;
				
				arrx1 = x2 + (int) (Math.cos(Math.toRadians(270 + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(270 + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(270 - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(270 - arrowAngle)) * arrowLenght);
			}
			else if (deltaY > 0) {
				y1 += circleRadius;
				y2 -= circleRadius;
				
				arrx1 = x2 + (int) (Math.cos(Math.toRadians(90 + arrowAngle)) * arrowLenght);
				arry1 = y2 - (int) (Math.sin(Math.toRadians(90 + arrowAngle)) * arrowLenght);
				arrx2 = x2 + (int) (Math.cos(Math.toRadians(90 - arrowAngle)) * arrowLenght);
				arry2 = y2 - (int) (Math.sin(Math.toRadians(90 - arrowAngle)) * arrowLenght);
			} else {
				arrx1 = x2;
				arry1 = y2;
				arrx2 = x2;
				arry2 = y2;
			}
		}
		// System.out.println(arrx1);
		g.drawLine(x1, y1, x2, y2);
		g.drawLine(x2, y2, arrx1, arry1);
		g.drawLine(x2, y2, arrx2, arry2);
		g.drawLine(arrx1, arry1, arrx2, arry2);
	}

	public void addNode(Graphics g, String TName) {
		int x = 150 * (nodeCount % 3) + 150;
		int y = 150 * (nodeCount / 3);
		int width = g.getFontMetrics().stringWidth(TName);

		g.setColor(new Color(249, 166, 2));
		g.fillOval(x, y, 2 * circleRadius, 2 * circleRadius);
		g.setColor(new Color(0, 0, 0));
		g.drawString(TName, x - width / 2 + circleRadius, y + circleRadius + 4);
		nodeCount++;
	}
}
