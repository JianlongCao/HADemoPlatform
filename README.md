h1. Setup XMPP to RESTful convertor

Suggest to run the convertor in the PC connected to TES301, if you are running it in the PC in Cisco network, you need to set port forwarding for port 8090 to XB3 in the gateway(TES301).
Steps to run the convertor:


h3. Step 1 : Prepare for the source codes or executable jar file


Source file: [http://gitlab.cisco.com/jiancao/demoplatform|http://gitlab.cisco.com/jiancao/demoplatform]

Executable jar file: [https://cisco.box.com/s/od5iuijk9abh1ckm6xguzdtt1cfez66g]

h3. Step 2 : Build Project (If you are using executable jar, please skip this step)

This project was built by maven.
{code}
# cd demo/
# ls    //demo.iml  pom.xml  src
# mvn clean install //will download dependencies in maven repo, if you got any error like timeout, please retry
# ls target/   //demo-1.0-SNAPSHOT-jar-with-dependencies.jar
{code}

h3. Step 3 : change default setting

There are default setting files in Jar, they are: settings.json, rules.json.
You could choose both ways to modify those files:
1. [Modify a jar file with UI |http://www.oracle.com/webfolder/technetwork/tutorials/obe/fmw/oim/oim_11g/customize_oim_ui_button_labels/modjar_in_ui.htm]
2. In source code, modify src/main/resources/settings.json, then build

Settings.json contains XMPP Server IP, Sctpa Server, XMPP Account for this project, XMPP Account for Openhab, XMPP Account for Notification User :
{code}
{
  "sctpa":{
    "SERVERIP" : "http://64.104.161.59:8090"
  },

  "EXCUTIONUNIT" : "sctpa",

  "openhab": {
    "XMPPSERVER": "173.39.210.33",    //XMPP registration, like Openfire Server
    "XMPPPORT": 5222,                 //default XMPP port
    "APPID": "clg002",                //Account for this project
    "OPENHABID": "clg003",            //Account for Openhab Server
    "USERID" : "clg001",              //Account for who will be notified when rules match
    "XMPPPWD": "Cisco123!"            //Password for APPID
  }
}
{code}

rules.json contains several presetting rules, suppose you have two devices, one is an openclose sensor, uri is [http://64.104.161.59:8090/devices/zw/2] in sctpa rs; another one is a hue light, uri is [http://64.104.161.59:8090/devices/zb/0017880100e7821e], we could try write some simple rules like blow code, which means if ContactSwitch opened, then turn hue to red color:
{code}
{
"If": {
        "addr":"2",    //sensor address
        "action": "open", //action: open, close, on, off, red, blue, green...
        "name": "ContactSwitch",
        "radio": "zw",   //sensor radio
        "message":"Door open, check live video : http://64.104.161.111:8080/getcamera"   //send to user
      },
"Then": {
        "addr": "0017880100e7821e",  //hue address
        "action": "red",
        "name": "Hue",
        "radio": "zb"   //radio
      }
},
...
}
{code}
presetting 4 rules:
If ContactSwitch is close, Then Hue will on
If ContactSwitch is close, Then Hue will green
If ContactSwitch is open, Then Hue will red
If ContactSwitch is open, Then Hue will blink

h3. Step 4 : Change OpenHab default address binding pattern
{code}
# cd {openhab_deployed}/configurations/sitemaps
# vim clg_demo.sitemap
please change the below HDM_ZIGBEE_XXXX to SCTPA_ZB_XXXX,  or HDM_ZWAVE_XXXX to SCTPA_ZW_XXXX
[\**Now we only care for hue*\*|**Now we only care for hue**]
sitemap demo label="CDBU CLG"
{
Frame {
                /*Prosyst HDM*/
                /*
                Slider item=HDM_ZIGBEE_0017880100E7821E_11
                Colorpicker item=HDM_ZIGBEE_0017880100E7821E_11_RGB icon="slider"
                */
                /*SCTPA*/
                Slider item=SCTPA_ZB_0017880100E7821E_11
                Colorpicker item=SCTPA_ZB_0017880100E7821E_11_RGB icon="slider"

                Group item=FF_Demo_Room label="More Devices" icon="firstfloor"
        }
....
}
# cd{openhab_deployed}/configurations/items
# vim clg.items
please change the below HDM_ZIGBEE_XXXX to SCTPA_ZB_XXXX,  or HDM_ZWAVE_XXXX to SCTPA_ZW_XXXX
[\**Now we only care for hue*\*|**Now we only care for hue**]
/\* Lights \*/
/\* Prosyst HDM \*/
/\*
Dimmer HDM_ZIGBEE_0017880100E7821E_11 "Hue Level"               (FF_Demo_Room) {clg="test"}
Color  HDM_ZIGBEE_0017880100E7821E_11_RGB "Hue Color"                   <slider> (FF_Demo_Room) {clg=""}
\*/
/\* Sctpa \*/
Dimmer SCTPA_ZB_0017880100E7821E_11 "Hue Level"                 (FF_Demo_Room) {clg="test"}
Color  SCTPA_ZB_0017880100E7821E_11_RGB "Hue Color"                     <slider> (FF_Demo_Room) {clg=""}
{code}

h3. Step 5 : Running
{code}
# cd demo/
# java \-jar target/demo-1.0-SNAPSHOT-jar-with-dependencies.jar
//print in console ...

GlobalConfig{sctpa=SCTPA{SERVERIP='http://64.104.161.59:8090'}, EXCUTIONUNIT='sctpa', openhab=OPENHAB{XMPPSERVER='173.3
9.210.33', XMPPPORT=5222, APPID='clg002', OPENHABID='clg003', XMPPPWD='Cisco123\!'}}
App: Waiting for connect OpenHab Server
App: OpenHab Server is starting
insert rule : Rule \---\- If ContactSwitch is close, Then Hue will on
insert rule : Rule \---\- If ContactSwitch is close, Then Hue will green
insert rule : Rule \---\- If ContactSwitch is open, Then Hue will red
insert rule : Rule \---\- If ContactSwitch is open, Then Hue will blink
Starting the Simple [] server on port 8111
enter something to exit

Press anything will exit the app.
{code}
