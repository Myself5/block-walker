package com.gooogle.code.blockWalker;

import java.util.Iterator;

import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.util.Debug;

import com.badlogic.gdx.physics.box2d.Body;


public class MapManager {
	private String currentMapNumber;

	private TMXMap map;
	
	public TMXMap getMap() {
		return map;
	}

	public String getCurrentMapNumber() {
		return currentMapNumber;
	}

	public MapManager (String startLocation){
		currentMapNumber = startLocation;
		map = new TMXMap(startLocation);

	}
	
	public void nextMap(Player pplayer){ 

		//detach all physic bodies EXCEPT PLAYER
		PhysicsWorld mPhysicsWorld = Resources.getmPhysicsWorld();

        Iterator<Body> iter = mPhysicsWorld.getBodies();
        final PhysicsConnector playerPhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(pplayer);
        while (iter.hasNext())
        {
        	Body temp = iter.next();
        	if (!temp.equals(playerPhysicsConnector.getBody()))
        	mPhysicsWorld.destroyBody(temp);
        	else 
        	{
        		Debug.d("PLAYER PHYSIC SAVED ! at the end of map " + currentMapNumber);
        	}
        }

        //detach all children
		Resources.getmScene().detachChildren();
		//clear goal watcher lists too ? ?? 
        
        System.gc();
		
        //make string new map file name
		// works now under 10 maps. 
		int i = Integer.parseInt(currentMapNumber.substring(5,6));
		i++;
		currentMapNumber = currentMapNumber.substring(0,5) + i + ".tmx";
		
		//create new map and add back player
		map = new TMXMap(currentMapNumber);
		Resources.getmScene().attachChild(pplayer);
		//can't seem to reposition player =(
		pplayer.rePosition();
		pplayer.setVisible(true);
	}
 
}