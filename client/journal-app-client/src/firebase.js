import {initializeApp} from 'firebase/app'
import {addDoc, collection, getFirestore} from 'firebase/firestore'
import {GoogleAuthProvider, getAuth, signInWithRedirect, getRedirectResult} from 'firebase/auth'
import {getStorage,ref, getDownloadURL} from 'firebase/storage'

// INIT 
const firebaseConfig={
    apiKey: process.env.REACT_APP_FIREBASE_API_KEY,
    authDomain: process.env.REACT_APP_FIREBASE_AUTH_DOMAIN,
    projectId: process.env.REACT_APP_FIREBASE_PROJECT_ID,
    storageBucket: process.env.REACT_APP_FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.REACT_APP_FIREBASE_MESSAGE_SENDER_ID,
    appId: process.env.REACT_APP_FIREBASE_APP_ID,
    measurementId: process.env.REACT_APP_FIREBASE_MEASUREMENT_ID
}

const initFirebaseApp = initializeApp(firebaseConfig);

// AUTH
export const auth = getAuth();
export const googleAuthProvider = new GoogleAuthProvider();



// FIRESTORE
export function loginWithGoogle(){
    signInWithRedirect(auth,googleAuthProvider);
    return getRedirectResult(auth);
}


const db = getFirestore();
export const journalRef = collection(db,'journals');
export const journalEntriesRef = collection(db,'journal_entries');


export function createDoc(collectionName,payload){
    let collectionRef = collection(db,collectionName);
    addDoc(collectionRef,payload);
}


// STORAGE
export const storage = getStorage();
export const imageLocRef= ref(storage,process.env.REACT_APP_FIREBASE_STORAGE_BUCKET);
export const getImageURL = (imageName) =>{
    let imageRef = ref(imageLocRef,imageName);
    
    getDownloadURL(imageRef).then((url)=>{
        return url;
    }).catch((err)=>{
        console.log(err.message)
    });
} 

export default initFirebaseApp;




