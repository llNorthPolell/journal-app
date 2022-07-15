import {auth,loginWithGoogle, signOut} from '../firebase'


export function loginToApp() {
    return loginWithGoogle();
}

export async function logoutOfApp() {
    try{
        await signOut();
        return "success";
    }catch(err){
        console.log("Error occurred while signing out... " + err.message);
    }

}

export function getAuth(){
    return auth;
}




