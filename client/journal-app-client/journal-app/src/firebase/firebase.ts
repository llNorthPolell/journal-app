import {initializeApp} from 'firebase/app';
import {addDoc, collection, getFirestore, getDocs,getDoc, connectFirestoreEmulator } from 'firebase/firestore';
import {GoogleAuthProvider, getAuth, signInWithRedirect, getRedirectResult, connectAuthEmulator } from 'firebase/auth';
import {getStorage,ref, getDownloadURL, uploadBytes, connectStorageEmulator } from 'firebase/storage';
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

// AUTH
export const auth = getAuth();
export const googleAuthProvider = new GoogleAuthProvider();


export function loginWithGoogle(){
    signInWithRedirect(auth,googleAuthProvider);
    return getRedirectResult(auth);
}

export function signOut(){
    return auth.signOut();
}


// FIRESTORE
const db = getFirestore();
/*export const journalRef = collection(db,process.env.REACT_APP_FIRESTORE_JOURNALS_COLLECTION);
export const journalEntriesRef = collection(db,process.env.REACT_APP_FIRESTORE_JOURNAL_ENTRIES_COLLECTION);
export const dashboardWidgetConfigRef = collection(db, process.env.REACT_APP_FIRESTORE_DASHBOARD_WIDGET_CONFIG_COLLECTION);
export const goalsRef = collection(db,process.env.REACT_APP_FIRESTORE_GOALS_COLLECTION);*/
/*
export async function createDoc(collectionRef:string,payload:any){
    let docPromise = await addDoc(collectionRef,payload);
    return docPromise;
}

export async function getList(query:string) {
    let output : any[]=[];
    try{
        let snapshot = await getDocs(query);
        snapshot.docs.forEach((doc:any) => {
            output.push({ ...doc.data(),key: doc.id });
        });
    }
    catch (err:any) {
        console.log(err.message);
    }
    return output;
}

export async function get(ref:string){
    try{
        let snapshot = await getDoc(ref);
        return snapshot.data();
    }
    catch (err:any) {
        console.log(err.message);
    }
}
*/
export {query,where,doc, updateDoc, arrayUnion, orderBy} from 'firebase/firestore';

// STORAGE
export const storage = getStorage();

export async function getStorageDownloadURL(fileName : string) {
    let fileRef = ref(storage,fileName);
    let fileURL = null;
    try{
        fileURL=await getDownloadURL(fileRef)
    }
    catch(err : any){
        console.log(err.message)
    }
    return fileURL;
} 


export async function uploadFile(file : File){
    let newFileName = v4()+"."+file.name.split('.').pop();
    let newFileRef = ref(storage,newFileName);
    let fileDownloadURL = null;
    await uploadBytes(newFileRef,file);
    fileDownloadURL = await getStorageDownloadURL(newFileName);
    console.log("File " + file + " has been uploaded to " + fileDownloadURL);
    return fileDownloadURL;
}


function shouldConnectToEmulatorHost(): boolean
{
    return process.env.NEXT_PUBLIC_CLIENT_HOST=="localhost";
}


if (shouldConnectToEmulatorHost()){
    console.log("LOCALHOST DETECTED!!!!");
    connectAuthEmulator(auth,"http://localhost:9099",{disableWarnings: true});
    connectFirestoreEmulator(db,"localhost",8080);
    connectStorageEmulator(storage,"localhost",9199);
}



export default app;




