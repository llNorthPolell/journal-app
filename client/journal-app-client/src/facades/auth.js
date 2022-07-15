import {auth,loginWithGoogle, signOut} from '../firebase'


export function loginToApp() {
    return loginWithGoogle();
}

export async function logoutOfApp() {
    try{
        await signOut();
    }catch(err){
        console.log("Error occurred while signing out... " + err.message);
    }

}

export function getAuth(){
    return auth;
}




