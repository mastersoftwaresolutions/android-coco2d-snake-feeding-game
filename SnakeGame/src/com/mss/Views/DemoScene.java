/**
 * @author acer- sarbjot Singh
 * 
 */
package com.mss.Views;

import java.util.List;
import java.util.Random;

import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCDelayTime;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.layers.CCColorLayer;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCScene;
import org.cocos2d.menus.CCMenu;
import org.cocos2d.menus.CCMenuItemLabel;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.opengl.CCBitmapFontAtlas;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.ccColor4B;

import android.content.Context;
import android.view.MotionEvent;

public class DemoScene extends CCLayer {
	private Context appcontext;
	private CGSize screenSize;
	float snakeSpeed = 0.010f;
	private float generalscalefactor = 0.0f;
	private static final int TILE_NODE_TAG = 20;
	private static final int PAUSE_OVERLAY_TAG = 25;
	private CCNode gameScreenNode;
	private CCSprite snakeHead;
	private CCSprite upIcon;
	private CCSprite downIcon;
	private CCNode leftIcon;
	private CCNode rightIcon;
	private float gameWidth;
	private float gameHeight;
	private float time;
	private float buttonLayoutHeight;
	private CCSprite food;
	private int count = 0;
	private CCNode snakebodyNode;
	private CCSprite tile;
	public static boolean gameover = false;

	public DemoScene() {
		this.setIsTouchEnabled(true);
		screenSize = CCDirector.sharedDirector().winSize();
		appcontext = CCDirector.sharedDirector().getActivity();
		generalscalefactor = CCDirector.sharedDirector().winSize().height / 500;
		addGameScreen();
		addSnakeHead();
		addFoodToScreen();
		controlButtons();
		schedule("update", 0.1f);
	}

	public static CCScene scene() {
		CCScene scene = CCScene.node();
		CCLayer layer = new DemoScene();
		scene.addChild(layer);
		return scene;
	}

	private void addGameScreen() {
		gameScreenNode = CCSprite.sprite("back.png");
		gameScreenNode.setScale(1f);
		gameScreenNode.setAnchorPoint(CGPoint.ccp(0f, 1f));
		gameScreenNode.setPosition(CGPoint.ccp(0, screenSize.height));
		addChild(gameScreenNode, -4);
		gameWidth = screenSize.width;
		gameHeight = gameScreenNode.getContentSize().height;
		buttonLayoutHeight = screenSize.height - gameHeight;
	}

	private void addSnakeHead() {
		snakeHead = CCSprite.sprite("snakeHead.png");
		snakeHead.setScale(0.8f);
		snakeHead.setPosition(CGPoint.ccp(screenSize.width / 2.0f,
				screenSize.height / 2.0f));
		// adding snakes parts to incress parts
		snakebodyNode = CCNode.node();
		snakebodyNode.setTag(TILE_NODE_TAG);
		snakeHead.addChild(snakebodyNode, -3);
		addChild(snakeHead, -3);
	}

	private void addFoodToScreen() {
		food = CCSprite.sprite("food.png");
		food.setScale(1f);

		food.setPosition(getRandomnumber(400), getRandomnumber(400)
				+ buttonLayoutHeight);
		addChild(food, -4);
	}

	private void controlButtons() {
		upIcon = CCSprite.sprite("upIcon.png");
		upIcon.setScale(0.7f);
		upIcon.setAnchorPoint(CGPoint.ccp(0f, 1f));
		upIcon.setPosition(CGPoint.ccp(screenSize.width / 2 - 50, 200));
		addChild(upIcon, 0);
		downIcon = CCSprite.sprite("downIcon.png");
		downIcon.setScale(0.7f);
		downIcon.setAnchorPoint(CGPoint.ccp(0f, 1f));
		downIcon.setPosition(CGPoint.ccp(screenSize.width / 2 - 50, 100));
		addChild(downIcon, 0);
		leftIcon = CCSprite.sprite("leftIcon.png");
		leftIcon.setScale(0.7f);
		leftIcon.setAnchorPoint(CGPoint.ccp(0f, 1f));
		leftIcon.setPosition(CGPoint.ccp(screenSize.width / 2 - 150, 150));
		addChild(leftIcon, 0);
		rightIcon = CCSprite.sprite("rightIcon.png");
		rightIcon.setScale(0.7f);
		rightIcon.setAnchorPoint(CGPoint.ccp(0f, 1f));
		rightIcon.setPosition(CGPoint.ccp(screenSize.width / 2 + 50, 150));
		addChild(rightIcon, 0);
	}

	public void GameOver() {
		int snakeX = (int) snakeHead.getPosition().x;
		int snakeY = (int) snakeHead.getPosition().y;
		System.out.println("x  :" + snakeX + " and y : " + snakeY);
		if (snakeX == 0) {
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.cheer);
			System.out.println("game over on slide left ");
			WinCallback();
		} else if (snakeX >= gameWidth - snakeHead.getContentSize().width) {
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.cheer);
			System.out.println("game over snakeY==screenSize.width");
			WinCallback();
		} else if (snakeY >= screenSize.height) {
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.cheer);
			System.out.println("game over on up");
			WinCallback();
		} else if (snakeY <= buttonLayoutHeight
				+ snakeHead.getContentSize().height) {
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.cheer);
			System.out.println("game over on down");
			WinCallback();
		}
		System.out.println("game over on action end");

	}

	@Override
	public boolean ccTouchesBegan(MotionEvent event) {

		CGPoint location = CCDirector.sharedDirector().convertToGL(
				CGPoint.ccp(event.getX(), event.getY()));
		System.out.println("Loction click : x " + event.getX() + " y "
				+ event.getY());
		CGRect spriteUpPos = CGRect.make(upIcon.getPosition().x * 1.35f
				- (upIcon.getContentSize().width / 2.0f),
				upIcon.getPosition().y
						- (upIcon.getContentSize().height / 2.0f),
				upIcon.getContentSize().width * 0.7f,
				upIcon.getContentSize().height * 0.7f);

		CGRect spriteDownPos = CGRect.make(downIcon.getPosition().x * 1.35f
				- (downIcon.getContentSize().width / 2.0f),
				downIcon.getPosition().y * 0.80f
						- (downIcon.getContentSize().height / 2.0f),
				downIcon.getContentSize().width * 0.7f,
				downIcon.getContentSize().height * 0.7f);

		CGRect spriteLeftPos = CGRect.make(leftIcon.getPosition().x * 1.55f
				- (leftIcon.getContentSize().width / 2.0f),
				leftIcon.getPosition().y * 0.85f
						- (leftIcon.getContentSize().height / 2.0f),
				leftIcon.getContentSize().width * 0.7f,
				leftIcon.getContentSize().height * 0.7f);

		CGRect spriteRightPos = CGRect.make(rightIcon.getPosition().x * 1.25f
				- (rightIcon.getContentSize().width / 2.0f),
				rightIcon.getPosition().y * 0.85f
						- (rightIcon.getContentSize().height / 2.0f),
				rightIcon.getContentSize().width * 0.7f,
				rightIcon.getContentSize().height * 0.7f);

		if (spriteUpPos.contains(location.x, location.y)) {
			// moveSnake(0);

			snakeHead.stopAllActions();
			snakeHead.setRotation(0);
			snakeHead.setRotation(270);
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.tileclick);
			time = (screenSize.height - snakeHead.getPosition().y) * snakeSpeed;
			snakeHead.runAction(CCSequence.actions(CCMoveTo.action(
					time,
					CGPoint.make(snakeHead.getPosition().x, screenSize.height
							- snakeHead.getContentSize().width / 2)),
					CCCallFunc.action(this, "GameOver")));

			System.out.println("touch on icon spriteUpPos ");

		} else if (spriteDownPos.contains(location.x, location.y)) {
			// moveSnake(1);
			snakeHead.stopAllActions();
			snakeHead.setRotation(0);
			snakeHead.setRotation(90);
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.tileclick);
			time = (snakeHead.getPosition().y - buttonLayoutHeight)
					* snakeSpeed;
			snakeHead.runAction(CCSequence.actions(CCMoveTo.action(
					time,
					CGPoint.make(snakeHead.getPosition().x, buttonLayoutHeight
							+ snakeHead.getContentSize().height / 2)),
					CCCallFunc.action(this, "GameOver")));

			System.out.println("touch on icon spriteDownPos  ");

		} else if (spriteLeftPos.contains(location.x, location.y)) {
			// moveSnake(2);
			snakeHead.stopAllActions();
			snakeHead.setRotation(0);
			snakeHead.setRotation(180);
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.tileclick);
			time = (snakeHead.getPosition().x) * snakeSpeed;
			snakeHead.runAction(CCSequence.actions(
					CCMoveTo.action(time, CGPoint.make(
							snakeHead.getContentSize().width / 2,
							snakeHead.getPosition().y)),
					CCCallFunc.action(this, "GameOver")));
			System.out.println("touch on icon spriteLeftPos ");

		} else if (spriteRightPos.contains(location.x, location.y)) {
			// moveSnake(3);
			snakeHead.stopAllActions();
			snakeHead.setRotation(0);
			SoundEngine.sharedEngine().playEffect(appcontext,
					com.mss.snakeFeeding.R.raw.tileclick);
			time = (gameWidth - snakeHead.getPosition().x) * snakeSpeed;
			snakeHead.runAction(CCSequence.actions(CCMoveTo.action(
					time,
					CGPoint.make(gameWidth - snakeHead.getContentSize().width
							/ 2, snakeHead.getPosition().y)), CCCallFunc
					.action(this, "GameOver")));
			System.out.println("touch on icon spriteRightPos ");
		} else {
			System.out.println("outside click on screen");
		}
		return true;
	}

	private void moveSnake(int moveTo) {
		List<CCNode> allChilds = snakeHead.getChildren();

		if (moveTo == 0) {
			for (int i = 0; i < allChilds.size(); i++) {
				CCNode firstNode = allChilds.get(i);
				// move head to next node to Left Side
				snakeHead.setPosition(snakeHead.getPosition().x,
						snakeHead.getPosition().y
								+ snakeHead.getContentSize().height);

				System.out.println("move to 0");
				firstNode.setPosition(
						firstNode.getPosition().x
								+ snakeHead.getContentSize().width
								+ snakeHead.getContentSize().width,
						firstNode.getPosition().y);

				firstNode.setPosition(firstNode.getPosition().x,
						firstNode.getPosition().y
								- snakeHead.getContentSize().width);
			}
			// } else if (moveTo == 1) {
			// for (int i = 0; i < allChilds.size(); i++) {
			// CCNode firstNode = allChilds.get(i);
			// // move head to next node to downward
			// snakeHead.setPosition(snakeHead.getPosition().x,
			// snakeHead.getPosition().y);
			// System.out.println("move to 1");
			// firstNode.setPosition(
			// firstNode.getPosition().x
			// - snakeHead.getContentSize().width,
			// firstNode.getPosition().y);
			// firstNode.setPosition(firstNode.getPosition().x,
			// firstNode.getPosition().y
			// - snakeHead.getContentSize().width);
			// }
			// } else if (moveTo == 2) {
			// for (int i = 0; i < allChilds.size(); i++) {
			// CCNode firstNode = allChilds.get(i);
			// // move head to next node to upward
			// snakeHead.setPosition(
			// snakeHead.getPosition().x
			// + snakeHead.getContentSize().width,
			// snakeHead.getPosition().y
			// - snakeHead.getContentSize().width);
			// System.out.println("move to 2");
			// firstNode.setPosition(firstNode.getPosition().x,
			// firstNode.getPosition().y
			// + snakeHead.getContentSize().width);
			// firstNode.setPosition(firstNode.getPosition().x,
			// firstNode.getPosition().y
			// - snakeHead.getContentSize().width);
			// }
			// } else if (moveTo == 3) {
			// System.out.println("move to 3");
			// for (int i = 0; i < allChilds.size(); i++) {
			// CCNode firstNode = allChilds.get(i);
			// // move head to next node to upward
			// snakeHead.setPosition(
			// snakeHead.getPosition().x
			// + snakeHead.getContentSize().width,
			// snakeHead.getPosition().y
			// - snakeHead.getContentSize().width);
			// System.out.println("move to 2");
			// firstNode.setPosition(firstNode.getPosition().x,
			// firstNode.getPosition().y
			// + snakeHead.getContentSize().width);
			// firstNode.setPosition(firstNode.getPosition().x,
			// firstNode.getPosition().y
			// - snakeHead.getContentSize().width);
			// }
			//
		}
		// firstNode.setPosition(
		// firstNode.getPosition().x + snakeHead.getContentSize().width,
		// firstNode.getPosition().x);
	}

	public void backCallback(Object sender) {
		CCColorLayer pauselayer = (CCColorLayer) getChildByTag(PAUSE_OVERLAY_TAG);
		pauselayer.runAction(CCMoveTo.action(
				0.2f,
				CGPoint.make(screenSize.width / 2.0f, screenSize.height
						+ pauselayer.getContentSize().height
						* generalscalefactor)));

		pauselayer.removeAllChildren(true);
		pauselayer.removeSelf();
		CCDirector.sharedDirector().replaceScene(DemoScene.scene());

	}

	private void WinCallback() {
		snakeHead.removeSelf();
		CCColorLayer pauseOverlay = CCColorLayer.node(ccColor4B.ccc4(25, 25,
				25, 255));
		pauseOverlay.setOpacity(200);
		pauseOverlay.setIsTouchEnabled(true);
		addChild(pauseOverlay, 200, PAUSE_OVERLAY_TAG);

		CCBitmapFontAtlas label = CCBitmapFontAtlas.bitmapFontAtlas(
				"TAP to continue!", "bionic.fnt");
		CCMenuItemLabel item5 = CCMenuItemLabel.item(label, this,
				"backCallback");
		CCMenu resumemenu = CCMenu.menu(item5);
		resumemenu.setPosition(100, -resumemenu.getContentSize().height * 0.6f
				* generalscalefactor);
		pauseOverlay.addChild(resumemenu, 800);
		resumemenu.runAction(CCSequence.actions(CCDelayTime.action(0.9f),
				CCMoveTo.action(0.5f, CGPoint.make(100, 100))));
	}

	public void update(float dt) {
		int foodLocationItemX = (int) (food.getPosition().x - food
				.getContentSize().width / 2);
		int foodLocationItemY = (int) (food.getPosition().y);

		CGRect spriteSnake = CGRect.make(
				snakeHead.getPosition().x - (snakeHead.getContentSize().width),
				snakeHead.getPosition().y
						- (snakeHead.getContentSize().height / 2.0f),
				snakeHead.getContentSize().width * 0.7f,
				snakeHead.getContentSize().height * 0.7f);

		if (spriteSnake.contains(foodLocationItemX, foodLocationItemY)
				|| spriteSnake.contains(
						foodLocationItemX - food.getContentSize().width / 2,
						foodLocationItemY)
				|| spriteSnake.contains(foodLocationItemX, foodLocationItemY
						- food.getContentSize().width / 2)
				|| spriteSnake.contains(
						foodLocationItemX - food.getContentSize().width / 2,
						foodLocationItemY - food.getContentSize().width / 2)) {
			food.removeSelf();
			addSnakebody();
			food.setPosition(getRandomnumber(400), getRandomnumber(400)
					+ buttonLayoutHeight);
			addChild(food, -4);
			System.out.println("Eat food");
		}
	}

	public int getRandomnumber(int sizeMax) {
		Random rand = new Random();
		return rand.nextInt(sizeMax);
	}

	void addSnakebody() {
		count++;
		tile = CCSprite.sprite("snakeBody.png");
		tile.setPosition(
				(snakebodyNode.getPosition().x + snakeHead.getContentSize().width / 2)
						- snakeHead.getContentSize().width * count,
				snakebodyNode.getPosition().y
						+ snakeHead.getContentSize().width / 2);
		snakeHead.addChild(tile, 1, 1);

	}
}
