import {uploadFile} from '../firebase/firebase'


export const defaultImgUrl = "https://firebasestorage.googleapis.com/v0/b/journal-app-75df1.appspot.com/o/defaultImg.png?alt=media&token=caa93413-9a70-47df-978e-bc787ec05378";

export const uploadImage = async (file: Blob) => {
    try {
        console.log("Image to save: " + file.name);
        const fileBuffer = await file.arrayBuffer();
        const savedFileURL = await uploadFile(file.name,fileBuffer);
        if (!savedFileURL){
            console.log("Something went wrong. Image may have been saved, but was not able to fetch the URL. Returning defaul file URL instead.")
            return defaultImgUrl;
        }
        return savedFileURL;
    }
    catch (error) {
        console.log("Failed to upload file. Returning default file URL instead.");
        console.log("Error: " + error);
        return defaultImgUrl;
    }
}
