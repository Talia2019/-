# 외부 서비스 정보

### Openvidu: `2.20`

### 1. Install Openvidu

[source](https://docs.openvidu.io/en/stable/tutorials/openvidu-js-java/)

1. Clone the repo

```bash
git clone <https://github.com/OpenVidu/openvidu-tutorials.git> -b v2.20.0
```

1. Run the tutorial

```bash
npm install -g http-server

http-server openvidu-tutorials/openvidu-hello-world/web
```

1. OpenVidu Platform service must be up and running in your development machine. The easiest way is running this Docker container which wraps both of them (you will need [Docker CE](https://store.docker.com/search?type=edition&offering=community)):

```bash
# WARNING: this container is not suitable for production deployments of OpenVidu Platform
# Visit <https://docs.openvidu.io/en/stable/deployment>

docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-server-kms:2.20.0
```

1. Go to *`[<http://localhost:8080>](<http://localhost:8080/>)`* to test the app once the server is running. The first time you use the docker container, an alert message will suggest you accept the self-signed certificate of *openvidu-server* when you first try to join a video-call.



### 2. Openvidu Call

```bash
docker run -p 4443:4443 --rm -e OPENVIDU_SECRET=MY_SECRET openvidu/openvidu-server-kms:2.20.0
```

**BE (Nodejs)**

https://docs.openvidu.io/en/stable/demos/openvidu-call/

```bash
git clone <https://github.com/OpenVidu/openvidu-call.git>
cd openvidu-call/openvidu-call-back
npm install
npm run start
```

**FE (Angular)**

```bash
cd openvidu-call/openvidu-call-front
npm install
npx ng serve
```

**error by not installing Angular**

```bash
npm i -g @angular/cli 
ng update @angular/cli @angular/core --allow-dirty --force
```

**FE frame(React)**

https://docs.openvidu.io/en/stable/demos/openvidu-call-react/

```bash
git clone <https://github.com/OpenVidu/openvidu-call-react.git>
cd openvidu-call-react/openvidu-call-react
npm install
npm start
```



### 3. Openvidu Call - Back

## Openvidu Call - Back

- controllers : CallController
  - REST API - Allow get an Openvidu token
- services : OpenViduService
  - Allows connect with Openvidu REST API
- app
  - Main file where our backend is launched
- config
  - The place where the envaironment variables are defined

**CallController**

```tsx
//service declare
const openviduService = new OpenViduService();

// postMapping
app.post('/', async (req: Request, res: Response) => {
	let sessionId: string = req.body.sessionId;
//session create
	let createdSession: Session = null;
	console.log('Session ID received', sessionId);
	try {
//session create
		createdSession = await openviduService.createSession(sessionId);
	} catch (error) {
		handleError(error, res);
		return;
	}
	try {
//create connection on created session
		const connection = await openviduService.createConnection(createdSession);
//send response 200 status code by token
		res.status(200).send(JSON.stringify(connection.token));
	} catch (error) {
		handleError(error, res);
	}
});

function handleError(error: any, res: Response){
	try {
		let statusCode = parseInt(error.message);
		res.status(parseInt(error.message)).send(`OpenVidu Server returned an error to OpenVidu Call Server: ${statusCode}`)
	} catch (error) {
		res.status(503).send('Cannot connect with OpenVidu Server');
	}
}
```

1. OpenViduService

```tsx
//Service에서 Openvidu 초기화, session/connection 생성
export class OpenViduService {

    private openvidu: OpenVidu;

	constructor(){
        this.openvidu = new OpenVidu(OPENVIDU_URL, OPENVIDU_SECRET);
    }

	public async createSession(sessionId: string): Promise<Session> {
        console.log("Creating session: ", sessionId);
        let sessionProperties: SessionProperties = {customSessionId: sessionId};
        return await this.openvidu.createSession(sessionProperties);
	}

	public async createConnection(session: Session): Promise<Connection> {
        console.log("Requesting token from session ", session);
        let connectionProperties: ConnectionProperties = {};
        return await session.createConnection(connectionProperties);
    }
}
```

**app**

```tsx
dotenv.config();
const app = express();

app.use(express.static('public'));
app.use(express.json());

app.use('/call', callController);

// Accept selfsigned certificates if CALL_OPENVIDU_CERTTYPE=selfsigned
if (CALL_OPENVIDU_CERTTYPE === 'selfsigned') {
    process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0"
}

app.listen(SERVER_PORT, () => {
    console.log("---------------------------------------------------------");
    console.log(" ")
    console.log(`OPENVIDU URL: ${OPENVIDU_URL}`);
    console.log(`OPENVIDU SECRET: ${OPENVIDU_SECRET}`);
    console.log(`CALL OPENVIDU CERTTYPE: ${CALL_OPENVIDU_CERTTYPE}`);
    console.log(`OpenVidu Call Server is listening on port ${SERVER_PORT}`);
    console.log(" ")
    console.log("---------------------------------------------------------");
});
```

**config**

```tsx
export const SERVER_PORT = process.env.SERVER_PORT || 5000;
export const OPENVIDU_URL = process.env.OPENVIDU_URL || '<https://localhost:4443>';
export const OPENVIDU_SECRET = process.env.OPENVIDU_SECRET || 'MY_SECRET';
export const CALL_OPENVIDU_CERTTYPE = process.env.CALL_OPENVIDU_CERTTYPE;
```



### 4. Openvidu Call - Front (Angular)

- home : Login Component
- components
  - chat : ChatComponent
  - room-config : RoomConfigComponent
  - StreamComponent : Compoment where the videos live
- models : Custom Objects used in openvidu call project
- services
  - chat : here is all chat logic, send and receive massages
  - devices : it manages the audio and video devices
  - network : service that allow contact with openvidu rest api
  - openvidu-session : it has the full control of the local perticipants and their openvidu sessions
  - remote-users : it has the full control of the remote participants



### 5. Openvidu Rest API

https://docs.openvidu.io/en/stable/reference-docs/REST-API/

