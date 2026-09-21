/*
    Mad Kaz   Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz.

    Mad Kaz is free software; you can redistribute it and/or modify it under 
    the terms of the GNU General Public License as published by the Free 
    Software Foundation; either version 3 of the License, or (at your option) 
    any later version.

    Mad Kaz is distributed in the hope that it will be useful, but WITHOUT ANY 
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
    FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
    details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package game;

import framework.cydAdvancedSprite;
import framework.cydAutomatedScene;
import framework.cydGameCanvas;
import framework.cydGraphicsManager;
import framework.cydImageHeaderMenuWindowOutput;
import framework.cydPointInterpolator;
import framework.cydSprite;
import framework.cydTokenizer;
import imagemanip.cydImageResize;
import java.io.DataInputStream;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import vm.cydByteArrayInputStream;
import vm.cydVMEngine;

public class cydGamePlayCanvas extends cydGameCanvas {
    public static final int BLOCK_TYPE_NONE                     = 0x0;
    public static final int BLOCK_TYPE_NORM                     = 0x1;
    public static final int BLOCK_TYPE_AIR                      = 0x2;
    public static final int BLOCK_TYPE_SPRING                   = 0x3;
    public static final int BLOCK_TYPE_DISAPPEARING             = 0x4;
    public static final int BLOCK_TYPE_SPIKE                    = 0x5;
    public static final int BLOCK_TYPE_ELECTRIC                 = 0x6;
    public static final int BLOCK_TYPE_UNKNOWN_1                = 0x7;
    public static final int BLOCK_TYPE_UNKNOWN_2                = 0x8;
    public static final int BLOCK_TYPE_UNKNOWN_3                = 0x9;
    public static final int BLOCK_TYPE_UNKNOWN_4                = 0xA;
    public static final int BLOCK_TYPE_UNKNOWN_5                = 0xB;
    public static final int BLOCK_TYPE_UNKNOWN_6                = 0xC;
    public static final int BLOCK_TYPE_UNKNOWN_7                = 0xD;
    public static final int BLOCK_TYPE_UNKNOWN_8                = 0xE;
    public static final int BLOCK_TYPE_UNKNOWN_9                = 0xF;
    
    public static final int IMAGE_ID_WALK                       = 0;
    public static final int IMAGE_ID_BLOCK_NORM                 = 1;
    public static final int IMAGE_ID_BLOCK_AIR                  = 2;
    public static final int IMAGE_ID_BLOCK_SPRING               = 3;
    public static final int IMAGE_ID_BLOCK_DISAPPEARING         = 4;
    public static final int IMAGE_ID_BLOCK_SPIKE                = 5;
    public static final int IMAGE_ID_BLOCK_ELECTRIC             = 6;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_1            = 7;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_2            = 8;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_3            = 9;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_4            = 10;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_5            = 11;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_6            = 12;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_7            = 13;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_8            = 14;
    public static final int IMAGE_ID_BLOCK_UNKNOWN_9            = 15;
    
    
    
    
    // game play flags
    public static final int GAMEPLAY_BITS_DIE_STATE_MASK             = 0x3;         // bb
    public static final int GAMEPLAY_BITS_DIE_STATE_NONE             = 0x0;         // 00
    public static final int GAMEPLAY_BITS_DIE_STATE_ON_TOP_HIT       = 0x1;         // 01
    public static final int GAMEPLAY_BITS_DIE_STATE_ON_BOTTOM_HIT    = 0x2;         // 10
    public static final int GAMEPLAY_BITS_DIE_STATE_ON_BOTH_HIT      = 0x3;         // 11
    
    public static final int GAMEPLAY_BITS_CAMERA_STATE_MASK          = 0x1C;        // bbbxx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_MIDDLE = 0x00;        // 000xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_TOP    = 0x04;        // 001xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_BOTTOM = 0x08;        // 010xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_STICK_TOP     = 0x0C;        // 011xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_STICK_BOTTOM  = 0x10;        // 100xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_RATE_ASCEND   = 0x14;        // 101xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_RATE_DESCEND  = 0x18;        // 110xx
    public static final int GAMEPLAY_BITS_CAMERA_STATE_RATE_CONSTANT = 0x1C;        // 111xx
    
    // param for GAMEPLAY_BITS_CAMERA_STATE_RATE_ASCEND = number of ms to wait before adding 1
    // param for GAMEPLAY_BITS_CAMERA_STATE_RATE_DESCEND = number of ms to wait before subing 1
    // param for GAMEPLAY_BITS_CAMERA_STATE_RATE_CONSTANT = camera position
    public static final int GAMEPLAY_BITS_CAMERA_PARAM_MASK          = 0xFFFFFFE0;  // bbbb bbbb bbbb bbbb bbbb bbbb bbbx xxxx
    public static final int GAMEPLAY_BITS_CAMERA_PARAM_SHIFT_AMOUNT  = 5;
    
    public static final int DEFAULT_GAMEPLAY_ONE_FLAGS                   = 0x0000001;
    
    public int m_gamePlayFlagsOne;
    public int m_cameraRateTimeElapsed = 0;
    
    
    
    
    public static final int GAMEPLAY_BITS_CONTROL_MASK                        = 0x3F;        // bbbbbb
    public static final int GAMEPLAY_BITS_CONTROL_INPUT_POWER                 = 0x1 << 0;    // 000001
    public static final int GAMEPLAY_BITS_CONTROL_INPUT_LEFT                  = 0x1 << 1;    // 000010
    public static final int GAMEPLAY_BITS_CONTROL_INPUT_RIGHT                 = 0x1 << 2;    // 000100
    public static final int GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_POWER        = 0x1 << 3;    // 001000
    public static final int GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_LEFT         = 0x1 << 4;    // 010000
    public static final int GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_RIGHT        = 0x1 << 5;    // 100000
    
    public static final int DEFAULT_GAMEPLAY_TWO_FLAGS               = 0x0000001;
    
    public int m_gamePlayFlagsTwo;
    
    
    public cydGameManager m_gm;
    public cydGraphicsManager m_gfxMnger;
    
    public int m_elapsedTime;
    
    public static final int INCREMENT_POWER_TIME = 800;
    
    public cydPointInterpolator m_builtUpInterpolator;
    public int m_currentView;
    public int m_levelsToDisplay;
    
    public static final int GAMESTATE_PLAYING                       = 0;
    public static final int GAMESTATE_PAUSED_MENU                   = 1;
    public static final int GAMESTATE_PAUSED                        = 2;
    public static final int GAMESTATE_GAME_FINISHED                 = 3;
    
    public int m_gameState = 0;
    
    
    
    
    // physics
    public int m_accumulatedGravTime;
    public int m_gravAmount;
    public int m_gravTime;
    
    public int m_objectCount;
    public int []m_objectProperties;                 // 0 = x pos, 1 = y pos, 2 = x movement time, 3 = y movement time, 4 = x accumlated time, 5 = y accumulated time, 6 = x movement amount, 7 = y movement amount, 8 = left bound, 9 = right bound, 10 = currently standing on block
    public cydAdvancedSprite []m_objectSprites;      // sprites
    public int [][][]m_objectSpriteSequences;        // lwalk, rwalk, llandwalk, rllandwalk, ljumpfly, rjumpfly, lfly, rfly, lwalk_waittimes, rwalk_waittimes, llandwalk_waittimes, rllandwalk_waittimes, ljumpfly_waittimes, rjumpfly_waittimes, lfly_waittimes, rfly_waittimes
    public boolean []m_objectAffectedByGrav;         // false = object isn't affected, true = is affected
    
    
    
    
    public static final int SPRITE_MODE_DIRECTION_MASK            = 0x1;
    public static final int SPRITE_MODE_DIRECTION_LEFT            = 0x1;
    public static final int SPRITE_MODE_DIRECTION_RIGHT           = 0x0;
    public static final int SPRITE_MODE_SEQUENCE_MASK             = 0x2;
    public static final int SPRITE_MODE_SEQUENCE_FLY              = 0x2;
    public static final int SPRITE_MODE_SEQUENCE_WALK             = 0x0;
    
    
    
    
    public static final int [][]WALK_LEFT_SEQUENCE                = new int [][] { {7, 8, 9, 8, 7, 10, 11, 10} };
    public static final int [][]WALK_RIGHT_SEQUENCE               = new int [][] { {0, 1, 2, 1, 0, 3, 4, 3} };
    public static final int [][]LAND_LEFT_SEQUENCE                = new int [][] { {7, 12, 7}, {7, 8, 9, 8, 7, 10, 11, 10} };
    public static final int [][]LAND_RIGHT_SEQUENCE               = new int [][] { {0, 5, 0}, {0, 1, 2, 1, 0, 3, 4, 3} };
    public static final int [][]JUMP_LEFT_SEQUENCE                = new int [][] { {7, 12, 7, 13}, {13} };
    public static final int [][]JUMP_RIGHT_SEQUENCE               = new int [][] { {0, 5, 0, 6}, {6} };
    public static final int [][]FLY_LEFT_SEQUENCE                 = new int [][] { {13} };
    public static final int [][]FLY_RIGHT_SEQUENCE                = new int [][] { {6} };
    
    public static final int [][]WALK_LEFT_WAIT_TIMES_SEQUENCE     = new int [][] { {160, 160, 160, 160, 160, 160, 160, 160} };
    public static final int [][]WALK_RIGHT_WAIT_TIMES_SEQUENCE    = new int [][] { {160, 160, 160, 160, 160, 160, 160, 160} };
    public static final int [][]LAND_LEFT_WAIT_TIMES_SEQUENCE     = new int [][] { {40, 40, 40}, {160, 160, 160, 160, 160, 160, 160, 160} };
    public static final int [][]LAND_RIGHT_WAIT_TIMES_SEQUENCE    = new int [][] { {40, 40, 40}, {160, 160, 160, 160, 160, 160, 160, 160} };
    public static final int [][]JUMP_LEFT_WAIT_TIMES_SEQUENCE     = new int [][] { {40, 40, 40, 40}, {160} };
    public static final int [][]JUMP_RIGHT_WAIT_TIMES_SEQUENCE    = new int [][] { {40, 40, 40, 40}, {160} };
    public static final int [][]FLY_LEFT_WAIT_TIMES_SEQUENCE      = new int [][] { {160} };
    public static final int [][]FLY_RIGHT_WAIT_TIMES_SEQUENCE     = new int [][] { {160} };
    
    
    
    
    
    
    
    
    public static final char []LOADING_MAIN_IMAGES    = "Loading Main Images...".toCharArray();
    public static final char []LOADING_SCENE_IMAGES   = "Loading Scene Images...".toCharArray();
    public static final char []LOADING_LEVEL          = "Generating Level...".toCharArray();
    public static final char []LOADING_LEVEL_2        = "Initializing Level...".toCharArray();
    
    public int []m_realLevelData;
    public int m_currentlyStandOnBlockType = BLOCK_TYPE_NONE;
    
    public cydSprite []m_blockSprites = new cydSprite[15];
    public int []m_blockProperties = new int[15 << 2];      // draw offset x, draw offset y,  startcollision offset, end collision offset
    
    public Image m_headerImage;
    
    public static final int DEFAULT_WALK_FRAME_HEIGHT           = 20;
    public static final int DEFAULT_WALK_FRAME_WIDTH            = 20;
    public static final int DEFAULT_WALK_FRAME_HEIGHT_OFFSET    = 0;
    public static final int DEFAULT_WALK_FRAME_WIDTH_OFFSET     = 0;
    
    public static final int DEFAULT_BLOCK_FRAME_WAIT_TIME       = 750;
    public static final int DEFAULT_BLOCK_FRAME_HEIGHT          = 10;
    public static final int DEFAULT_BLOCK_FRAME_WIDTH           = 42;
    public static final int []DEFAULT_BLOCK_ANIM_SEQUENCE       = null;
    
    public static final int BLOCK_FRAME_X_OFFSET                = 19;
    
    
    
    
    
    
    public static final int DEFAULT_POWER                       = 0x00000090;
    
    
    
    
    public int m_shakeFactor                                    = 0;
    public Random m_random                                      = new Random(System.currentTimeMillis());
    
    public int []m_timers                                       = null;
    public int m_onDieAddress                                   = 0;
    public int m_onLeftHitAddress                               = 0;
    public int m_onRightHitAddress                              = 0;
    public int []m_onBlockDepartAddresses                       = new int[16];
    public int []m_onBlockStepAddresses                         = new int[16];
    
    
    
    public static final int VM_NO_ENTRY_POINT                   = 0;
    
    
    
    
    // pause menu itmes
    public static final int INDEX_PAUSE_TITLE                   = 0;
    public static final int INDEX_PAUSE_RESUME_OPTION           = 1;
    public static final int INDEX_PAUSE_MUSIC_OPTION            = 2;
    public static final int INDEX_PAUSE_EXIT_OPTION             = 3;
    
    public cydImageHeaderMenuWindowOutput m_pausedMenu          = null;
    
    public static final int SELECTION_PAUSE_RESUME_GAME         = 0;
    public static final int SELECTION_PAUSE_TOGGLE_MUSIC        = 1;
    public static final int SELECTION_PAUSE_EXIT_GAME           = 2;
    
    
    
    
    
    // saved
    public int m_savedSlowDownFactor;
    public int m_savedGameState;
    public int m_savedWhiteOutColor;
    public int m_savedShakeFactor;
    
    
    
    
    // vm data
    public byte []m_vmLevelData = null;
    public Hashtable m_vmGlobalHash;
    
    public byte []m_stackByte;
    public short []m_stackShort;
    public short []m_stackOperand;
    public int []m_stackInt;
    public long []m_stackLong;
    public Object []m_stackObject;
    public short []m_stackCall;
    public short []m_stackTrapData;
    public String []m_stackTrapType;
    
    
    
    
    // return code
    public static final int RETURN_CODE_NONE                            = 0;
    public static final int RETURN_CODE_PREMATURE_EXIT                  = 1;
    public static final int RETURN_CODE_LEVEL_PASSED                    = 2;
    public static final int RETURN_CODE_LEVEL_FAILED                    = 3;
    
    public int m_returnCode = RETURN_CODE_PREMATURE_EXIT;
    
    
    
    
    
    
    
    public static final String INPUT_JAR                                = "JAR";
    public static final String INPUT_RES                                = "RES";
    public static final String INPUT_JAR_RES                            = "JAR_RES";
    public static final String INPUT_NONE                               = "NONE";

    public static final int INVERT_SCOPE                                = 1 << 0;
    public static final int NIGHT_SCOPE                                 = 1 << 1;
    public static final int RED_SCOPE                                   = 1 << 2;
    public static final int GREEN_SCOPE                                 = 1 << 3;
    public static final int BW_SCOPE                                    = 1 << 4;

    public static final String GENERIC_SCALE                            = "GSCALE";
    public static final String BL_SCALE                                 = "BLSCALE";
    public static final String NN_SCALE                                 = "NNSCALE";
    
    
    public int m_enviromentEffects;
    
    
    
    
    
    
    
    
    public cydAutomatedScene m_scene;
    
    
    
    
    
    
    
    public cydGamePlayCanvas(cydGameManager gm, byte []vmLevelData, int enviromentEffects, Hashtable vmGlobalHash) {
        super(gm.m_font);
        
        m_gm = gm;
        m_vmLevelData = vmLevelData;
        m_enviromentEffects = enviromentEffects;
        m_vmGlobalHash = vmGlobalHash;
    }
    
    public void saveVitalFieldsAndGotoPauseMenu() {
        m_savedSlowDownFactor = m_slowDownFactor;
        m_savedGameState = m_gameState;
        m_savedWhiteOutColor = m_whiteOutColor;
        m_savedShakeFactor = m_shakeFactor;
        
        m_savedSlowDownFactor = 0;
        m_shakeFactor = 0;
        m_whiteOutColor = -1;
        m_gameState = cydGamePlayCanvas.GAMESTATE_PAUSED_MENU;
    }
    
    public void restoreVitalFields() {
        m_slowDownFactor = m_savedSlowDownFactor;
        m_gameState = m_savedGameState;
        m_whiteOutColor = m_savedWhiteOutColor;
        m_shakeFactor = m_savedShakeFactor;
    }
    
    public void setup() {
        setBackground(0xFFFFFF);
        
        m_gm.m_player.stopMusic();
        m_gm.m_player.closeMusic();
        
        loadingScreen(LOADING_LEVEL);
        flushGraphics();
        
        
        
        // run script
        byte []buffer = null;
        
        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, cydVMEngine.findEntryPoint(m_vmLevelData), 32, 32, 32, 32, 32, 32, 4, 4);
        System.gc();
        
        
        
        // load level
        loadMainGraphics();
        loadInitialLevelDetails();
        loadSpritesAndBoundsData();
        
        byte []resVMData = cydGameManager.getISToByteArray(getClass().getResourceAsStream("/gp_res.script"));
        loadPausedMenu(resVMData);
        
        callSceneGraphicsSetup();
        callPostProcessSetup();
        
        System.gc();
    }
    
    public void stop() {
        if (m_gm.m_musicOn) {
            m_gm.m_player.stopMusic();
            m_gm.m_player.closeMusic();
        }
    }
    
    public void loadPausedMenu(byte []vmData) {
        cydByteArrayInputStream vmDataIS = new cydByteArrayInputStream(vmData);
        DataInputStream vmDataDIS = new DataInputStream(vmDataIS);
        
        char []title = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PAUSE_TITLE);
        
        char [][]menuOptions = new char[3][];
        
        menuOptions[0] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PAUSE_RESUME_OPTION);
        menuOptions[1] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PAUSE_MUSIC_OPTION);
        menuOptions[2] = cydGameManager.readResourceString(vmDataIS, vmDataDIS, INDEX_PAUSE_EXIT_OPTION);
        
        m_pausedMenu = new cydImageHeaderMenuWindowOutput(m_gm.m_font, title, null, menuOptions, null, null, 25, 25, m_width - 50, m_height - 50, 0x0000FF);
    }
    
    public void loadInitialLevelDetails() {
        loadingScreen(LOADING_LEVEL_2);
        flushGraphics();
        
        // load script results
        m_realLevelData = (int [])m_vmGlobalHash.get("mapData");
        int maxTimers = ((int [])m_vmGlobalHash.get("maxTimers"))[0];
        int []stackLength = (int [])m_vmGlobalHash.get("stackLengths");
        
        
        // timer setup
        m_timers = new int[maxTimers << 2];
        
        
        
        // stack setup
        m_stackByte = new byte[stackLength[0]];
        m_stackShort = new short[stackLength[1]];
        m_stackOperand = new short[stackLength[2]];
        m_stackInt = new int[stackLength[3]];
        m_stackLong = new long[stackLength[4]];
        m_stackObject = new Object[stackLength[5]];
        m_stackCall = new short[stackLength[6] * 15];
        m_stackTrapData = new short[stackLength[7] * 16];
        m_stackTrapType = new String[stackLength[7]];
        
        
        
        // game basics setup
        m_headerImage = cydGameManager.generateHeaderBar(m_width, 11, m_gm.m_font);
        m_builtUpInterpolator = new cydPointInterpolator(1, new int [] { 0, 11 }, new int [] { INCREMENT_POWER_TIME, 0 }, 0, false);
        
        
        
        // view setup
        int addAmountToDisplay = (m_height % DEFAULT_BLOCK_FRAME_HEIGHT == 0 ? 0 : 1);
        m_levelsToDisplay = (m_height / DEFAULT_BLOCK_FRAME_HEIGHT) + addAmountToDisplay + 1;
        m_currentView = (m_realLevelData.length * DEFAULT_BLOCK_FRAME_HEIGHT) - m_height;
        
        
        
        // setup
        m_objectCount = 1;
        
        m_objectProperties = new int[m_objectCount << 5];
        m_objectAffectedByGrav = new boolean[m_objectCount];
        
        m_gamePlayFlagsOne = DEFAULT_GAMEPLAY_ONE_FLAGS;
        m_gamePlayFlagsTwo = DEFAULT_GAMEPLAY_TWO_FLAGS;
        
        setObject(0, 0, (m_realLevelData.length * DEFAULT_BLOCK_FRAME_HEIGHT) - m_height + 50, 23, 30, 1, 1, 0, 176, -1, -1, DEFAULT_POWER, DEFAULT_WALK_FRAME_WIDTH_OFFSET, DEFAULT_WALK_FRAME_HEIGHT_OFFSET, false);
        
        m_gravAmount = 1;
        m_gravTime = 200;
    }
    
    public void callPostProcessSetup() {
        int postSetupEntryPoint = ((int [])m_vmGlobalHash.get("postSetupEntryPoint"))[0];
        
        if (postSetupEntryPoint != cydGamePlayCanvas.VM_NO_ENTRY_POINT) {
            cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, postSetupEntryPoint, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
            System.gc();
        }
    }
    
    public void loadMainGraphics() {
        loadingScreen(LOADING_MAIN_IMAGES);
        flushGraphics();
        
        Object []nameArray = (Object [])m_vmGlobalHash.get("imageIds");
        
        int imageCount = 0;
        
        Hashtable loadedImages = new Hashtable();
        
        m_gfxMnger = new cydGraphicsManager(17);
        
        for (int i = 0; i < 17; i++) {
            String imageName = (String)nameArray[i];
            
            if (imageName == null)
                continue;
            
            if (loadedImages.containsKey(imageName)) {
                int pos = ((Integer)loadedImages.get(imageName)).intValue();
                
                m_gfxMnger.copyImage(pos, i, false, true);
            } else {
                String []gfxInstructions = cydTokenizer.tokenize(imageName, ':');
                loadImage(m_gfxMnger, gfxInstructions, i);
                
                loadedImages.put(imageName, new Integer(i));
            }
        }
    }
    
    public void callSceneGraphicsSetup() {
        loadingScreen(LOADING_SCENE_IMAGES);
        flushGraphics();
        
        m_scene = new cydAutomatedScene();
        m_scene.resizeSet(2);
        
        int sceneSetupEntryPoint = ((int [])m_vmGlobalHash.get("sceneSetupEntryPoint"))[0];
        
        if (sceneSetupEntryPoint != cydGamePlayCanvas.VM_NO_ENTRY_POINT) {
            cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, sceneSetupEntryPoint, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
            System.gc();
        }
    }
    
    public byte []loadData(String insts) {
        String []instructions = cydTokenizer.tokenize(insts, ':');
        
        int elementCount = 0;
        byte []buf = null;
        
        String dataReadType = instructions[elementCount++];
        
        if (dataReadType.equals(cydGamePlayCanvas.INPUT_JAR)) {
            buf = cydGame.getISToByteArray(getClass().getResourceAsStream(instructions[elementCount++]));
        } else if (dataReadType.equals(cydGamePlayCanvas.INPUT_RES)) {
            int resId = Integer.parseInt(instructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(m_vmLevelData, resId);
            int size = ((m_vmLevelData[offset++] << 24) | ((m_vmLevelData[offset++] & 0xFF) << 16) | ((m_vmLevelData[offset++] &0xFF) << 8) | (m_vmLevelData[offset++] & 0xFF));
            
            buf = new byte[size];
            System.arraycopy(m_vmLevelData, 0, buf, 0, size);
        } else if (dataReadType.equals(cydGamePlayCanvas.INPUT_JAR_RES)) {
            byte []vmData = cydGame.getISToByteArray(getClass().getResourceAsStream(instructions[elementCount++]));
            int resId = Integer.parseInt(instructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(vmData, resId);
            int size = ((vmData[offset++] << 24) | ((vmData[offset++] & 0xFF) << 16) | ((vmData[offset++] &0xFF) << 8) | (vmData[offset++] & 0xFF));

            buf = new byte[size];
            System.arraycopy(vmData, offset, buf, 0, size);
        }
        
        return buf;
    }

    public void loadImage(cydGraphicsManager gfxMnger, String[] gfxInstructions, int imageNum) {
        int elementCount = 0;
        
        byte []buf = null;
        
        
        
        //
        // ADD IMAGE
        //
        
        String imageReadType = gfxInstructions[elementCount++];
        
        if (imageReadType.equals(cydGamePlayCanvas.INPUT_JAR)) {
            buf = cydGame.getISToByteArray(getClass().getResourceAsStream(gfxInstructions[elementCount++]));
            gfxMnger.addImage(imageNum, buf, false, 0, buf.length);
        } else if (imageReadType.equals(cydGamePlayCanvas.INPUT_RES)) {
            int imageId = Integer.parseInt(gfxInstructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(m_vmLevelData, imageId);
            int size = ((m_vmLevelData[offset++] << 24) | ((m_vmLevelData[offset++] & 0xFF) << 16) | ((m_vmLevelData[offset++] &0xFF) << 8) | (m_vmLevelData[offset++] & 0xFF));
            gfxMnger.addImage(imageNum, m_vmLevelData, false, offset, size);
        } else if (imageReadType.equals(cydGamePlayCanvas.INPUT_JAR_RES)) {
            buf = cydGame.getISToByteArray(getClass().getResourceAsStream(gfxInstructions[elementCount++]));
            int imageId = Integer.parseInt(gfxInstructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(buf, imageId);
            int size = ((buf[offset++] << 24) | ((buf[offset++] & 0xFF) << 16) | ((buf[offset++] &0xFF) << 8) | (buf[offset++] & 0xFF));
            gfxMnger.addImage(imageNum, buf, true, offset, size);
        }
        
        
        // 
        // PALETTE EFFECT
        // 
        
        String paletteReadType = gfxInstructions[elementCount++];
        
        if (paletteReadType.equals(cydGamePlayCanvas.INPUT_JAR)) {
            buf = cydGame.getISToByteArray(getClass().getResourceAsStream(gfxInstructions[elementCount++]));
            
            Vector v = new Vector();
            v.addElement(buf);
            v.addElement(new Integer(0));
            
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_SWITCH_PALETTE, v);
        } else if (paletteReadType.equals(cydGamePlayCanvas.INPUT_RES)) {
            int imageId = Integer.parseInt(gfxInstructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(m_vmLevelData, imageId);
            int size = ((m_vmLevelData[offset++] << 24) | ((m_vmLevelData[offset++] & 0xFF) << 16) | ((m_vmLevelData[offset++] &0xFF) << 8) | (m_vmLevelData[offset++] & 0xFF));
            
            Vector v = new Vector();
            v.addElement(m_vmLevelData);
            v.addElement(new Integer(offset));
            
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_SWITCH_PALETTE, v);
        } else if (paletteReadType.equals(cydGamePlayCanvas.INPUT_JAR_RES)) {
            buf = cydGame.getISToByteArray(getClass().getResourceAsStream(gfxInstructions[elementCount++]));
            int imageId = Integer.parseInt(gfxInstructions[elementCount++]);
            
            int offset = cydVMEngine.getResourceOffset(buf, imageId);
            int size = ((buf[offset++] << 24) | ((buf[offset++] & 0xFF) << 16) | ((buf[offset++] &0xFF) << 8) | (buf[offset++] & 0xFF));

            Vector v = new Vector();
            v.addElement(buf);
            v.addElement(new Integer(offset));
            
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_SWITCH_PALETTE, v);
        }
        
        boolean givesOffHeat = Integer.parseInt(gfxInstructions[elementCount++]) != 0 ? true : false;
        
        if ((m_enviromentEffects & cydGamePlayCanvas.INVERT_SCOPE) != 0) {
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_INVERT_COLORIZE, null);
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.NIGHT_SCOPE) != 0) {
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_NIGHT_TIME_COLORIZE, null);
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.BW_SCOPE) != 0) {
            gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_GRAYSCALE_COLORIZE, null);
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.RED_SCOPE) != 0) {
            if (givesOffHeat)
                gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_RED_HEAT_COLORIZE, null);
            else
                gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_RED_COLORIZE, null);
        } else if ((m_enviromentEffects & cydGamePlayCanvas.GREEN_SCOPE) != 0) {
            if (givesOffHeat)
                gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_GREEN_HEAT_COLORIZE, null);
            else
                gfxMnger.applyPaletteEffectSingle(imageNum, cydGraphicsManager.PALETTE_EFFECT_GREEN_COLORIZE, null);
        }
        
        
        Thread.yield();
        System.gc();
        Runtime.getRuntime().gc();
        Thread.yield();
        
        Thread.yield();
        gfxMnger.loadImage(imageNum);
        Thread.yield();
        
        
        
        String scaleType = gfxInstructions[elementCount++];
        
        if (scaleType.startsWith(cydGamePlayCanvas.GENERIC_SCALE)) {
            int width = Integer.parseInt(gfxInstructions[elementCount++]);
            int height = Integer.parseInt(gfxInstructions[elementCount++]);
            
            gfxMnger.applyEffect(imageNum, cydGraphicsManager.EFFECT_RESIZE, new int[] {width, height, m_gm.m_scalingType});
        } else if (scaleType.startsWith(cydGamePlayCanvas.BL_SCALE)) {
            int width = Integer.parseInt(gfxInstructions[elementCount++]);
            int height = Integer.parseInt(gfxInstructions[elementCount++]);
            
            gfxMnger.applyEffect(imageNum, cydGraphicsManager.EFFECT_RESIZE, new int[] {width, height, cydImageResize.BILINEAR});
        } else if (scaleType.startsWith(cydGamePlayCanvas.NN_SCALE)) {
            int width = Integer.parseInt(gfxInstructions[elementCount++]);
            int height = Integer.parseInt(gfxInstructions[elementCount++]);
            
            gfxMnger.applyEffect(imageNum, cydGraphicsManager.EFFECT_RESIZE, new int[] {width, height, cydImageResize.NORMAL});
        }
    }
    
    public void setBackground(int color) {
        int gray = (((color >>> 16) & 0xFF) + ((color >>> 8) & 0xFF) + (color & 0xFF)) / 3;
        
        if ((m_enviromentEffects & cydGamePlayCanvas.INVERT_SCOPE) != 0) {
            color = (~color) & 0x00FFFFFF ;
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.NIGHT_SCOPE) != 0) {
            color = (byte)(((gray & 0xFF) * 0x7F) >> 8);
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.BW_SCOPE) != 0) {
            color = gray | (gray << 8) | (gray << 16);
        }
        
        if ((m_enviromentEffects & cydGamePlayCanvas.RED_SCOPE) != 0) {
            color = 0x00FF0000 | (gray << 8) | gray;
        } else if ((m_enviromentEffects & cydGamePlayCanvas.GREEN_SCOPE) != 0) {
            color = 0x0000FF00 | (gray << 16) | gray;
        }
        
        super.setBackground(color);
    }
    
    public void loadSpritesAndBoundsData() {
        Object []blockWaitTimes = (Object [])m_vmGlobalHash.get("blockWaitTimes");         // object arrays full of int arrays
        Object []blockAnimSequences = (Object [])m_vmGlobalHash.get("blockAnimSequence");   // object arrays full of int arrays
        int []blockFrameHeights = (int [])m_vmGlobalHash.get("blockFrameHeights");
        int []blockFrameWidths = (int [])m_vmGlobalHash.get("blockFrameWidths");
        int []spriteFrameWidth = (int [])m_vmGlobalHash.get("spriteFrameWidth");
        
        
        m_objectSpriteSequences = new int [m_objectCount << 4][][];
        m_objectSprites = new cydAdvancedSprite[m_objectCount];
        
        // create game sprites out of images
        Image img = null;
        
        int spriteWidth = cydGamePlayCanvas.DEFAULT_WALK_FRAME_WIDTH;
        
        if (spriteFrameWidth != null)
            spriteWidth = spriteFrameWidth[0];
        
        img = m_gfxMnger.getImage(IMAGE_ID_WALK);
        setObjectSprites(0, WALK_LEFT_SEQUENCE, WALK_RIGHT_SEQUENCE, LAND_LEFT_SEQUENCE, LAND_RIGHT_SEQUENCE, JUMP_LEFT_SEQUENCE, JUMP_RIGHT_SEQUENCE, FLY_LEFT_SEQUENCE, FLY_RIGHT_SEQUENCE, WALK_LEFT_WAIT_TIMES_SEQUENCE, WALK_RIGHT_WAIT_TIMES_SEQUENCE, LAND_LEFT_WAIT_TIMES_SEQUENCE, LAND_RIGHT_WAIT_TIMES_SEQUENCE, JUMP_LEFT_WAIT_TIMES_SEQUENCE, JUMP_RIGHT_WAIT_TIMES_SEQUENCE, FLY_LEFT_WAIT_TIMES_SEQUENCE, FLY_RIGHT_WAIT_TIMES_SEQUENCE, img, spriteWidth);
        
        for (int i = 0; i < 15; i++) {
            if (!m_gfxMnger.exists(i + 1))
                continue;
            
            img = m_gfxMnger.getImage(i + 1);
            
            int blockFrameHeight = DEFAULT_BLOCK_FRAME_HEIGHT;
            int blockFrameWidth = DEFAULT_BLOCK_FRAME_WIDTH;
            int []blockFrameAnimSeq = DEFAULT_BLOCK_ANIM_SEQUENCE;
            
            if (blockFrameHeights != null) {
                if (blockFrameHeights[i] != -1)
                    blockFrameHeight = blockFrameHeights[i];
            }
            
            if (blockFrameWidths != null) {
                if (blockFrameWidths[i] != -1)
                    blockFrameWidth = blockFrameWidths[i];
            }
            
            if (blockAnimSequences != null) {
                blockFrameAnimSeq = (int [])blockAnimSequences[i];
            }
            
            int []blockFrameWaitTime = null;
            
            if (blockWaitTimes != null) {
                blockFrameWaitTime = (int [])blockWaitTimes[i];
            }
            
            if (blockFrameWaitTime == null) {
                int numOfFrames = img.getWidth() / blockFrameWidth;
                
                blockFrameWaitTime = new int[numOfFrames];
                
                for (int j = 0; j < numOfFrames; j++)
                    blockFrameWaitTime[j] = DEFAULT_BLOCK_FRAME_WAIT_TIME;
            }
            
            m_blockSprites[i] = new cydSprite(img, blockFrameWidth, blockFrameHeight, blockFrameAnimSeq, blockFrameWaitTime);
        }
    }
    
    public void loadingScreen() {
        loadingScreen("Loading...".toCharArray());
    }
    
    public void loadingScreen(char []msg) {
        m_graphics.setColor(0x000000);
        m_graphics.fillRect(0, 0, getWidth(), getHeight());
        
        int x = getWidth()/2 - m_gm.m_font.getWidth(msg)/2;
        int y = getHeight()/2 - m_gm.m_font.getHeight()/2;
        
        m_gm.m_font.setForegroundOutlineColor(0xFFFFFF);
        m_gm.m_font.drawString(m_graphics, msg, x, y, -1);
    }
    
    public void draw() {
        Graphics g = m_graphics;
        
        int shakeFactor = m_shakeFactor;
        int xFactor = 0;
        int yFactor = 0;
        
        m_scene.drawLayers(g, 0);
        
        if (shakeFactor != 0) {
            xFactor = m_random.nextInt() % shakeFactor;
            yFactor = m_random.nextInt() % shakeFactor;
            g.translate(xFactor, yFactor);
        }
        
        g.translate(0, -m_currentView);
        
        int len = m_realLevelData.length;
        
        int mapStartAt = m_currentView / DEFAULT_BLOCK_FRAME_HEIGHT;
        int mapEndAt = mapStartAt + m_levelsToDisplay;
        
        if (mapEndAt > len) {
            mapStartAt = len - m_levelsToDisplay;
            mapEndAt = len;
        } else if (mapStartAt < 0) {
            mapStartAt = 0;
            mapEndAt = m_levelsToDisplay;
        }
        
        int []realLevelData  = m_realLevelData;
        cydSprite []blockSprites = m_blockSprites;
        int []blockProps = m_blockProperties;
        
        int yScreenPos = mapStartAt * DEFAULT_BLOCK_FRAME_HEIGHT;
        
        for (int i = mapStartAt; i < mapEndAt; i++) {
            int currentFloor = realLevelData[i];
            
            for (int j = 0; j < 8; j++) {
                int block = (currentFloor >>> (j<<2)) & 0x0000000F;     // j * 4
                
                if (block == 0)
                    continue;
                
                block -= 1;
                
                blockSprites[block].setPosition(((7-j) * BLOCK_FRAME_X_OFFSET) + blockProps[block << 2], yScreenPos  + blockProps[(block << 2) | 1]);
                blockSprites[block].paint(g);
            }
            
            yScreenPos += DEFAULT_BLOCK_FRAME_HEIGHT;
        }
        
        m_objectSprites[0].paint(g);
        
        g.translate(0, m_currentView);
        
        if (shakeFactor != 0) {
            g.translate(-xFactor, -yFactor);
        }
        
        m_scene.drawLayers(g, 1);
        
        
        
        
        switch (m_gameState) {
            case cydGamePlayCanvas.GAMESTATE_PLAYING: {
                // draw status bar
                g.drawImage(m_headerImage, 0, 0, 0);
                g.setColor(0x0000FF);
                g.fillRect(26, 2, 5*m_builtUpInterpolator.getPoints()[0], 6);
            }
            break;
            case cydGamePlayCanvas.GAMESTATE_PAUSED_MENU: {
                m_pausedMenu.draw(g);
            }
            break;
            case cydGamePlayCanvas.GAMESTATE_PAUSED: {
                
            }
            break;
            case cydGamePlayCanvas.GAMESTATE_GAME_FINISHED: {
                
            }
            break;
        }
    }
    
    public boolean process(int timeElapsed) {
        int keyStates = getKeyStates();
        
        switch (m_gameState) {
            case cydGamePlayCanvas.GAMESTATE_PLAYING: {
                int []scrollerMovementSystemObjectProperties = m_objectProperties;
                
                processKeyInput(timeElapsed, scrollerMovementSystemObjectProperties, keyStates);
                processBlockAnimations(timeElapsed);
                processObjects(timeElapsed);
                
                int newX = scrollerMovementSystemObjectProperties[0];
                int newY = scrollerMovementSystemObjectProperties[1];
                
                int offsetX = scrollerMovementSystemObjectProperties[13];
                int offsetY = scrollerMovementSystemObjectProperties[14];
                
                processGamePlay(timeElapsed, newX + offsetX, newY + offsetY, m_gamePlayFlagsOne);
                processTimers(timeElapsed);
                
                m_scene.process(timeElapsed, m_currentView);
                
                if ((keyStates & GAME_A_PRESSED) != 0)
                    saveVitalFieldsAndGotoPauseMenu();
            }
            return true;
            case cydGamePlayCanvas.GAMESTATE_PAUSED_MENU: {
                int result = m_pausedMenu.process(timeElapsed, m_gm.m_keyInputSlowDowner.processKeyPress(keyStates, timeElapsed));
                
                switch (result) {
                    case cydGamePlayCanvas.SELECTION_PAUSE_RESUME_GAME: {
                        restoreVitalFields();
                        m_gameState = cydGamePlayCanvas.GAMESTATE_PLAYING;
                    }
                    break;
                    case cydGamePlayCanvas.SELECTION_PAUSE_TOGGLE_MUSIC: {
                        m_gm.m_player.toggleMusic();
                    }
                    break;
                    case cydGamePlayCanvas.SELECTION_PAUSE_EXIT_GAME: {
                        m_returnCode = cydGamePlayCanvas.RETURN_CODE_PREMATURE_EXIT;
                    }
                    return false;
                }
            }
            return true;
            case cydGamePlayCanvas.GAMESTATE_PAUSED: {
                if ((keyStates & GAME_A_PRESSED) != 0)
                    saveVitalFieldsAndGotoPauseMenu();
            }
            return true;
            case cydGamePlayCanvas.GAMESTATE_GAME_FINISHED: {
                
            }
            return false;
        }
        
        throw new RuntimeException("Unexpected game state :" + m_gameState);
    }
    
    public void processTimers(int timeElapsed) {
        byte []vmLevelData = m_vmLevelData;
        Hashtable vmGlobalHash = m_vmGlobalHash;
        byte []stackByte = m_stackByte;
        short []stackShort = m_stackShort;
        short []stackOperand = m_stackOperand;
        int []stackInt = m_stackInt;
        long []stackLong = m_stackLong;
        Object []stackObject = m_stackObject;
        short []stackCall = m_stackCall;
        short []stackTrapData = m_stackTrapData;
        String []stackTrapType = m_stackTrapType;
        
        int []timers = m_timers;
        
        int maxTimers = timers.length >> 2;
        
        for (int i = 0; i < maxTimers; i++) {
            int index = i << 2;
            int timerRepeatCount = timers[index | 3];
            
            if (timerRepeatCount == 0)
                continue;
            
            int timerHitTime = timers[index | 0];
            int timerElapsedTime = timers[index | 1] + timeElapsed;
            int timerVMAddress = timers[index | 2];
            
            
            if (timerElapsedTime >= timerHitTime) {
                timers[index | 1] = 0;
                
                if (timerRepeatCount > 0)
                    timers[index | 3] = timerRepeatCount - 1;
                
                cydVMEngine.run(this, vmLevelData, vmGlobalHash, timerVMAddress, stackByte, stackShort, stackOperand, stackInt, stackLong, stackObject, stackCall, stackTrapData, stackTrapType, -1, -1, -1, -1, -1, -1);
            } else {
                timers[index | 1] = timerElapsedTime;
            }
        }
    }
    
    public void processGamePlay(int timeElapsed, int playerX, int playerY, int flags) {
        int []objectProperties = m_objectProperties;
        
        // Set player position
        m_objectSprites[0].setPosition(playerX, playerY);
        
        
        // Update camera position
        int cameraMode = flags & GAMEPLAY_BITS_CAMERA_STATE_MASK;
        int maxCameraPos = (m_realLevelData.length * cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT) - m_height;
        int topCameraPos = 0;
        
        switch (cameraMode) {
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_MIDDLE: {
                topCameraPos = playerY - 100;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_TOP: {
                topCameraPos = playerY - 50;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_FOLLOW_BOTTOM: {
                topCameraPos = playerY - 150;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_STICK_TOP: {
                int newPos = playerY - 50;
                topCameraPos = m_currentView;
                
                if (newPos < topCameraPos)
                    topCameraPos = newPos;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_STICK_BOTTOM: {
                int newPos = playerY - 150;
                topCameraPos = m_currentView;
                
                if (newPos < topCameraPos)
                    topCameraPos = newPos;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_RATE_ASCEND: {
                topCameraPos = m_currentView;
                int param = (flags & GAMEPLAY_BITS_CAMERA_PARAM_MASK) >>> GAMEPLAY_BITS_CAMERA_PARAM_SHIFT_AMOUNT;
                
                int cameraRateTimeElapsed = m_cameraRateTimeElapsed + timeElapsed;
                
                while (cameraRateTimeElapsed >= param)  {
                    cameraRateTimeElapsed -= param;
                    topCameraPos--;
                }
                
                m_cameraRateTimeElapsed = cameraRateTimeElapsed;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_RATE_DESCEND: {
                topCameraPos = m_currentView;
                int param = (flags & GAMEPLAY_BITS_CAMERA_PARAM_MASK) >>> GAMEPLAY_BITS_CAMERA_PARAM_SHIFT_AMOUNT;
                
                int cameraRateTimeElapsed = m_cameraRateTimeElapsed + timeElapsed;
                
                while (cameraRateTimeElapsed >= param)  {
                    cameraRateTimeElapsed -= param;
                    topCameraPos++;
                }
                
                m_cameraRateTimeElapsed = cameraRateTimeElapsed;
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_CAMERA_STATE_RATE_CONSTANT: {
                int param = (flags & GAMEPLAY_BITS_CAMERA_PARAM_MASK) >>> GAMEPLAY_BITS_CAMERA_PARAM_SHIFT_AMOUNT;
                topCameraPos = param;
            }
            break;
        }
        
        if (topCameraPos < 0)
            topCameraPos = 0;
        else if (topCameraPos > maxCameraPos)
            topCameraPos = maxCameraPos;
        
        m_currentView = topCameraPos;
        
        
        
        //Check for offscreen deaths
        int deathMode = flags & GAMEPLAY_BITS_DIE_STATE_MASK;
        
        switch (deathMode) {
            case cydGamePlayCanvas.GAMEPLAY_BITS_DIE_STATE_ON_TOP_HIT: {
                if (playerY < topCameraPos) {
                    if (m_onDieAddress == cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        m_gameState = cydGamePlayCanvas.GAMESTATE_GAME_FINISHED;
                    else
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onDieAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                }
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_DIE_STATE_ON_BOTTOM_HIT: {
                if (playerY + objectProperties[19] > topCameraPos + m_height) {
                    if (m_onDieAddress == cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        m_gameState = cydGamePlayCanvas.GAMESTATE_GAME_FINISHED;
                    else
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onDieAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                }
            }
            break;
            case cydGamePlayCanvas.GAMEPLAY_BITS_DIE_STATE_ON_BOTH_HIT: {
                if (playerY < topCameraPos) {
                    if (m_onDieAddress == cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        m_gameState = cydGamePlayCanvas.GAMESTATE_GAME_FINISHED;
                    else
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onDieAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                } else if (playerY + objectProperties[19] > topCameraPos + m_height) {
                    if (m_onDieAddress == cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        m_gameState = cydGamePlayCanvas.GAMESTATE_GAME_FINISHED;
                    else
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onDieAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                }
            }
            break;
        }
    }
    
    public void processKeyInput(int timeElapsed, int []scrollerMovementSystemObjectProperties, int keyStates) {
        int gameFlagsTwo = m_gamePlayFlagsTwo;
        
        if (!m_objectAffectedByGrav[0]) {
            if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_INPUT_POWER) != 0) && ((keyStates & FIRE_PRESSED) != 0))
                m_builtUpInterpolator.interpolate(timeElapsed);
            else {
                int power = m_builtUpInterpolator.m_points[0];
                
                if (power != 0) {
                    int blockType = scrollerMovementSystemObjectProperties[15];
                    
                    if (m_onBlockDepartAddresses[blockType] != cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onBlockDepartAddresses[blockType], m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                    
                    scrollerMovementSystemObjectProperties[7] = -((scrollerMovementSystemObjectProperties[12] * (power << 8))) >> 16;
                    scrollerMovementSystemObjectProperties[1]--;
                    scrollerMovementSystemObjectProperties[10] = -1;
                    scrollerMovementSystemObjectProperties[11] = -1;
                    scrollerMovementSystemObjectProperties[15] = cydGamePlayCanvas.BLOCK_TYPE_NONE;
                    m_objectAffectedByGrav[0] = true;
                    
                    if (scrollerMovementSystemObjectProperties[7] < 0 && scrollerMovementSystemObjectProperties[17] != 0)
                        changeSpriteLaunch(0);
                }
                
                m_builtUpInterpolator.restart();
            }
            
            if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_INPUT_LEFT) != 0) && ((keyStates & LEFT_PRESSED) != 0)) {
                int rate = m_objectProperties[6];
                
                if (rate > 0) {
                    m_objectProperties[6] = rate * -1;
                    changeSpriteDirection(0);
                }
            } else if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_INPUT_RIGHT) != 0) && ((keyStates & RIGHT_PRESSED) != 0)) {
                int rate = m_objectProperties[6];
                
                if (rate < 0) {
                    m_objectProperties[6] = rate * -1;
                    changeSpriteDirection(0);
                }
            }
        } else {
            if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_POWER) != 0) && ((keyStates & FIRE_PRESSED) != 0))
                m_builtUpInterpolator.interpolate(timeElapsed);
            
            if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_LEFT) != 0) && ((keyStates & LEFT_PRESSED) != 0)) {
                int rate = m_objectProperties[6];
                
                if (rate > 0) {
                    m_objectProperties[6] = rate * -1;
                    changeSpriteDirection(0);
                }
            } else if (((gameFlagsTwo & cydGamePlayCanvas.GAMEPLAY_BITS_CONTROL_NON_COLL_INPUT_RIGHT) != 0) && ((keyStates & RIGHT_PRESSED) != 0)) {
                int rate = m_objectProperties[6];
                
                if (rate < 0) {
                    m_objectProperties[6] = rate * -1;
                    changeSpriteDirection(0);
                }
            }
        }
    }
    
    public void processBlockAnimations(int timeElapsed) {
        cydSprite []blockSprites = m_blockSprites;
        
        int len = blockSprites.length;
        
        for (int i = 0; i < len; i++) {
            cydSprite s = blockSprites[i];
            
            if (s != null)
                s.play(timeElapsed);
        }
    }
    
    public void processObjects(int timeElapsed) {
        int len = m_objectCount;
        boolean []objectAffectedByGrav = m_objectAffectedByGrav;
        int []objectProperties = m_objectProperties;
        int [][][]objectSequences = m_objectSpriteSequences;
        cydAdvancedSprite []objectSprites = m_objectSprites;
        int []blockProperties = m_blockProperties;
        
        for (int i = 0; i < len; i++) {
            int propInd = i << 5;
            cydAdvancedSprite sprite = objectSprites[i];
            
            
            
            // Sprite bounds
            int objWidth = objectProperties[propInd | 18];
            int objHeight = objectProperties[propInd | 19];
            
            
            
            // Calculate x-axis
            int origXPos = objectProperties[propInd | 0];
            int xMovementRate = objectProperties[propInd | 2];
            int xMovementAmount = objectProperties[propInd | 6];
            int xPos = origXPos;
            
            int xAccumulatedTime = objectProperties[propInd | 4];
            
            xAccumulatedTime += timeElapsed;
            
            if (xAccumulatedTime < xMovementRate) {         //  hasn't hit desired time yet
                objectProperties[propInd + 4] = xAccumulatedTime;
            } else {                                        //  hit desired time
                while (xAccumulatedTime >= xMovementRate) {     // turn into div / mod if benchmarks dont show much difference, this is probably fine.
                    xPos += xMovementAmount;
                    xAccumulatedTime -= xMovementRate;
                }
                
                int leftBound = objectProperties[propInd | 8];
                int rightBound = objectProperties[propInd | 9];
                
                if (xPos + objWidth > rightBound) {
                    int seqInd = i << 4;
                    
                    objectProperties[propInd | 6] = -xMovementAmount;
                    xPos = rightBound - objWidth;
                    
                    changeSpriteDirection(i);
                    
                    if (m_onLeftHitAddress != cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onLeftHitAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                } else if (xPos < leftBound) {
                    int seqInd = i << 4;
                    
                    objectProperties[propInd | 6] = -xMovementAmount;
                    xPos = leftBound;
                    
                    changeSpriteDirection(i);
                    
                    if (m_onRightHitAddress != cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onRightHitAddress, m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                }
                
                objectProperties[propInd | 0] = xPos;
                objectProperties[propInd | 4] = xAccumulatedTime;
            }
            
            
            
            // Calculate gravity
            int gravModifier = 0;
            
            int gravAmount = m_gravAmount;
            int gravAccumulatedTime = m_accumulatedGravTime + timeElapsed;
            int gravTime = m_gravTime;
            
            while (gravAccumulatedTime >= gravTime) {
                gravModifier += gravAmount;
                gravAccumulatedTime -= gravTime;
            }
            
            m_accumulatedGravTime = gravAccumulatedTime;
            
            
            
            
            // Calculate y-axis
            int origYPos = objectProperties[propInd | 1];
            int yPos = origYPos;
            
            int yMovementRate = objectProperties[propInd | 3];
            int yAccumulatedTime = objectProperties[propInd | 5];
            int yMovementAmount = objectProperties[propInd | 7];
            
            yAccumulatedTime += timeElapsed;
            
            if (objectAffectedByGrav[i])
                yMovementAmount += gravModifier;
            
            if (yAccumulatedTime < yMovementRate) {         //  hasn't hit desired time yet
                objectProperties[propInd | 5] = yAccumulatedTime;
            } else {                                        //  hit desired time
                while (yAccumulatedTime >= yMovementRate) {     // turn into div / mod if benchmarks dont show much difference, this is probably fine.
                    yPos += yMovementAmount;
                    yAccumulatedTime -= yMovementRate;
                }
            }
            
            objectProperties[propInd | 1] = yPos;
            objectProperties[propInd | 5] = yAccumulatedTime;
            objectProperties[propInd | 7] = yMovementAmount;
            
            
            
            
            // Calculate collision with blocks
            int standingOnBlockStartX = objectProperties[propInd | 10];
            int standingOnBlockEndX = objectProperties[propInd | 11];
            
            if (standingOnBlockStartX == -1 && standingOnBlockEndX == -1 && yMovementAmount >= 0) {
                int origYBottom = origYPos + objHeight;
                int nextYBlockPos = (origYBottom / cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT) * cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                
                int diffTillNext = origYBottom % cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                if (diffTillNext != 0)
                    nextYBlockPos += cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                
                int endYBlockPos = yPos + objHeight;
                
                while (nextYBlockPos <= endYBlockPos) {
                    int rowIndex = nextYBlockPos / cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                    
                    int []blockData = m_realLevelData;
                    int blockDataLen = blockData.length;
                    
                    if (rowIndex < blockDataLen && rowIndex >= 0) {
                        int rowData = blockData[rowIndex];
                        boolean done = false;
                        
                        int endXPos = xPos + objWidth;
                        
                        for (int j = 8; j > 0; j--) {
                            int blockID = (rowData >> (32 - (j << 2))) & 0x0000000F;
                            
                            if (blockID != cydGamePlayCanvas.BLOCK_TYPE_NONE) {
                                int ogXOffset = (j-1) * cydGamePlayCanvas.BLOCK_FRAME_X_OFFSET;
                                int xStartOffset = ogXOffset + blockProperties[(blockID << 2) | 2];
                                int xEndOffset = ogXOffset + cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_WIDTH + blockProperties[(blockID << 2) | 3];

                                if (endXPos >= xStartOffset && xPos <= xEndOffset) {
                                    int oldYMovementRate = objectProperties[propInd | 7];

                                    objectProperties[propInd | 10] = xStartOffset;
                                    objectProperties[propInd | 11] = xEndOffset;
                                    objectProperties[propInd | 1] = (rowIndex * cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT) - objHeight;
                                    objectProperties[propInd | 15] = blockID;
                                    objectAffectedByGrav[i] = false;

                                    if (m_onBlockStepAddresses[blockID] != cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onBlockStepAddresses[blockID], m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                                    
                                    if (objectAffectedByGrav[i] == false)       // done incase vm's told to ignore block collision
                                        objectProperties[propInd | 7] = 0;

                                    if (objectProperties[propInd | 20] != rowIndex) {
                                        changeSpriteStand(i);
                                        objectProperties[propInd | 20] = rowIndex;
                                    } else if (oldYMovementRate > 0 && objectProperties[propInd | 17] != 0) {
                                        changeSpriteStand(i);
                                    }
                                    
                                    done = true;
                                    break;
                                }
                            }
                        }
                        
                        if (done)
                            break;
                    }
                    
                    nextYBlockPos += cydGamePlayCanvas.DEFAULT_BLOCK_FRAME_HEIGHT;
                }
            } else {
                int endXPos = xPos + objWidth;
                
                if (endXPos < standingOnBlockStartX || xPos > standingOnBlockEndX) {
                    int blockType = objectProperties[propInd | 15];
                    
                    if (m_onBlockDepartAddresses[blockType] != cydGamePlayCanvas.VM_NO_ENTRY_POINT)
                        cydVMEngine.run(this, m_vmLevelData, m_vmGlobalHash, m_onBlockDepartAddresses[blockType], m_stackByte, m_stackShort, m_stackOperand, m_stackInt, m_stackLong, m_stackObject, m_stackCall, m_stackTrapData, m_stackTrapType, -1, -1, -1, -1, -1, -1);
                    
                    objectProperties[propInd | 10] = -1;
                    objectProperties[propInd | 11] = -1;
                    objectProperties[propInd | 15] = cydGamePlayCanvas.BLOCK_TYPE_NONE;
                    objectAffectedByGrav[i] = true;
                }
            }
            
            sprite.play(timeElapsed);
        }
    }
    
    public void changeSpriteStand(int i) {
        int propIndex = i << 5;
        int seqInd = i << 4;
        
        int mode = m_objectProperties[propIndex | 16];
        
        switch (mode & cydGamePlayCanvas.SPRITE_MODE_DIRECTION_MASK) {
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT: {
                m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;
                m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 3], m_objectSpriteSequences[seqInd | 11]);
            }
            break;
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT: {
                m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;
                m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 2], m_objectSpriteSequences[seqInd | 10]);
            }
            break;
        }
    }
    
    public void changeSpriteLaunch(int i) {
        int propIndex = i << 5;
        int seqInd = i << 4;
        
        int mode = m_objectProperties[propIndex | 16];
        
        switch (mode & cydGamePlayCanvas.SPRITE_MODE_DIRECTION_MASK) {
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT: {
                m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY;
                m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 5], m_objectSpriteSequences[seqInd | 13]);
            }
            break;
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT: {
                m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY;
                m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 4], m_objectSpriteSequences[seqInd | 12]);
            }
            break;
        }
    }
    
    public void changeSpriteDirection(int i) {
        int propIndex = i << 5;
        int seqInd = i << 4;
        
        int mode = m_objectProperties[propIndex | 16];
        
        switch (mode & cydGamePlayCanvas.SPRITE_MODE_DIRECTION_MASK) {
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT: {
                switch (mode & cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_MASK) {
                    case cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY: {
                        m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY;
                        m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 6], m_objectSpriteSequences[seqInd | 14]);
                    }
                    break;
                    case cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK: {
                        m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;
                        m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 0], m_objectSpriteSequences[seqInd | 8]);
                    }
                    break;
                }
            }
            break;
            case cydGamePlayCanvas.SPRITE_MODE_DIRECTION_RIGHT: {
                switch (mode & cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_MASK) {
                    case cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY: {
                        m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY;
                        m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 7], m_objectSpriteSequences[seqInd | 15]);
                    }
                    break;
                    case cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK: {
                        m_objectProperties[propIndex | 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;
                        m_objectSprites[i].setFrameSequence(m_objectSpriteSequences[seqInd | 1], m_objectSpriteSequences[seqInd | 9]);
                    }
                    break;
                }
            }
            break;
        }
    }
    
    public void setObject(int index, int xPos, int yPos, int xMovementTime, int yMovementTime, int xMovementAmount, int yMovementAmount, int leftBound, int rightBound, int leftStandingOnBlockStartXPos, int rightStandingOnBlockStartXPos, int power, int refX, int refY, boolean leftRightOnly) {
        m_objectAffectedByGrav[index] = true;
        
        int propIndex = index << 5;
        
        m_objectProperties[propIndex + 0] = xPos;
        m_objectProperties[propIndex + 1] = yPos;
        m_objectProperties[propIndex + 2] = xMovementTime;
        m_objectProperties[propIndex + 3] = yMovementTime;
        m_objectProperties[propIndex + 4] = 0;
        m_objectProperties[propIndex + 5] = 0;
        m_objectProperties[propIndex + 6] = xMovementAmount;
        m_objectProperties[propIndex + 7] = yMovementAmount;
        m_objectProperties[propIndex + 8] = leftBound;
        m_objectProperties[propIndex + 9] = rightBound;
        m_objectProperties[propIndex + 10] = leftStandingOnBlockStartXPos;
        m_objectProperties[propIndex + 11] = rightStandingOnBlockStartXPos;
        m_objectProperties[propIndex + 12] = power;
        m_objectProperties[propIndex + 13] = refX;
        m_objectProperties[propIndex + 14] = refY;
        m_objectProperties[propIndex + 15] = 0;            // standing on block id
        
        if (leftRightOnly) {
            m_objectProperties[propIndex + 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_WALK;       // sprite mode
            m_objectProperties[propIndex + 17] = 0;       // 0 = no, 1 or other = yes
        } else {
            m_objectProperties[propIndex + 16] = cydGamePlayCanvas.SPRITE_MODE_DIRECTION_LEFT | cydGamePlayCanvas.SPRITE_MODE_SEQUENCE_FLY;       // sprite mode
            m_objectProperties[propIndex + 17] = 1;       // 0 = no, 1 or other = yes
        }
        
        m_objectProperties[propIndex + 20] = -1;          // last row index stepped on
    }
    
    public void setObjectSprites(int index, int [][]leftWalkSequence, int [][]rightWalkSequence, int [][]leftLandSequence, int [][]rightLandSequence, int [][]leftJumpSequence,
            int [][]rightJumpSequence, int [][]leftFlySequence, int [][]rightFlySequence, int [][]leftWalkWaitTimes, int [][]rightWalkWaitTimes,
            int [][]leftLandWaitTimes, int [][]rightLandWaitTimes, int [][]leftJumpWaitTimes, int [][]rightJumpWaitTimes, int [][]leftFlyWaitTimes,
            int [][]rightFlyWaitTimes, Image img, int frameWidth) {
        int spriteIndex = index << 4;
        int propIndex = index << 5;
        
        if (m_objectProperties[propIndex + 17] != 0)
            m_objectSprites[index] = new cydAdvancedSprite(img, frameWidth, img.getHeight(), rightFlySequence, rightFlyWaitTimes);
        else
            m_objectSprites[index] = new cydAdvancedSprite(img, frameWidth, img.getHeight(), rightWalkSequence, rightWalkWaitTimes);
        
        m_objectSpriteSequences[spriteIndex + 0] = leftWalkSequence;
        m_objectSpriteSequences[spriteIndex + 1] = rightWalkSequence;
        m_objectSpriteSequences[spriteIndex + 2] = leftLandSequence;
        m_objectSpriteSequences[spriteIndex + 3] = rightLandSequence;
        m_objectSpriteSequences[spriteIndex + 4] = leftJumpSequence;
        m_objectSpriteSequences[spriteIndex + 5] = rightJumpSequence;
        m_objectSpriteSequences[spriteIndex + 6] = leftFlySequence;
        m_objectSpriteSequences[spriteIndex + 7] = rightFlySequence;
        m_objectSpriteSequences[spriteIndex + 8] = leftWalkWaitTimes;
        m_objectSpriteSequences[spriteIndex + 9] = rightWalkWaitTimes;
        m_objectSpriteSequences[spriteIndex + 10] = leftLandWaitTimes;
        m_objectSpriteSequences[spriteIndex + 11] = rightLandWaitTimes;
        m_objectSpriteSequences[spriteIndex + 12] = leftJumpWaitTimes;
        m_objectSpriteSequences[spriteIndex + 13] = rightJumpWaitTimes;
        m_objectSpriteSequences[spriteIndex + 14] = leftFlyWaitTimes;
        m_objectSpriteSequences[spriteIndex + 15] = rightFlyWaitTimes;
        
        m_objectProperties[propIndex + 18] = frameWidth;             // width of sprite
        m_objectProperties[propIndex + 19] = img.getHeight();        // height of sprite
    }
}
