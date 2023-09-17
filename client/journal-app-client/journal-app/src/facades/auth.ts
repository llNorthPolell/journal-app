import {auth,loginWithGoogle, signOut} from '../firebase/firebase'


export function loginToApp() {
    try{
        return loginWithGoogle();
    }
    catch(err:any){
        console.log("Error occurred while signing in... " + err.message);
    }
}

export async function logoutOfApp() {
    try{
        await signOut();
    }catch(err:any){
        console.log("Error occurred while signing out... " + err.message);
    }

}

export function getAuth(){
    return auth;
}


export type {User, Auth} from 'firebase/auth';

