import {initializeApp} from 'firebase/app';
import {getStorage,ref, getDownloadURL, uploadBytes, connectStorageEmulator, uploadBytesResumable } from 'firebase/storage';
import {v4} from 'uuid'

// INIT 
const firebaseConfig={
    apiKey: process.env.NEXT_PUBLIC_FIREBASE_API_KEY,
    authDomain: process.env.NEXT_PUBLIC_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.NEXT_PUBLIC_FIREBASE_PROJECT_ID,
    storageBucket: process.env.NEXT_PUBLIC_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.NEXT_PUBLIC_FIREBASE_MESSAGE_SENDER_ID,
    appId: process.env.NEXT_PUBLIC_FIREBASE_APP_ID,
    measurementId: process.env.NEXT_PUBLIC_FIREBASE_MEASUREMENT_ID
}

const app = initializeApp(firebaseConfig);

// STORAGE
export const storage = getStorage();

export async function getStorageDownloadURL(fileName : string) {
    try{
        const fileRef = ref(storage,fileName);
        const fileURL=await getDownloadURL(fileRef)
        return fileURL;
    }
    catch(err : any){
        console.log(err.message)
        return {};
    }
    
} 


export async function uploadFile(originalFileName : string, fileBuffer : ArrayBuffer) : Promise<string|{}>{
    const newFileName = v4()+"."+originalFileName.split('.').pop();
    const newFileRef = ref(storage,newFileName);
    await uploadBytes(newFileRef,fileBuffer);
    const fileDownloadURL = await getStorageDownloadURL(newFileName);
    console.log("File " + originalFileName + " has been uploaded to " + fileDownloadURL);
    return fileDownloadURL;
}


function shouldConnectToEmulatorHost(): boolean
{
    return process.env.NEXT_PUBLIC_CLIENT_HOST=="localhost";
}


if (shouldConnectToEmulatorHost()){
    console.log("LOCALHOST DETECTED!!!!");
    connectStorageEmulator(storage,"localhost",9199);
}



export default app;




