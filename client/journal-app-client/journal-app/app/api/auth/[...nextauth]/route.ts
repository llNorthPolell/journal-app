import { NextAuthOptions } from "next-auth";
import { JWT } from "next-auth/jwt";
import NextAuth from "next-auth/next";
import GoogleProvider from "next-auth/providers/google";

async function refreshGoogleToken(token: JWT){
    try {
        console.log("auth/[...nextauth]/route - Old Refresh Token: " + token.refresh_token);

        const url =
          "https://oauth2.googleapis.com/token?" +
          new URLSearchParams({
            client_id: process.env.GOOGLE_CLIENT_ID as string,
            client_secret: process.env.GOOGLE_CLIENT_SECRET as string,
            grant_type: "refresh_token" as string,
            refresh_token: token.refresh_token as string,
          })
    
        const response = await fetch(url, {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          method: "POST",
        })
    
        const refreshedTokens = await response.json()
        console.log("New Refresh Token: " + JSON.stringify(refreshedTokens));
        if (!response.ok) {
          throw refreshedTokens
        }
        
        console.log("auth/[...nextauth]/route - Token refreshed successfully");
        return {
          ...token,
          access_token: refreshedTokens.access_token,
          id_token: refreshedTokens.id_token,
          expires: Date.now() + refreshedTokens.expires_in * 1000,
          refresh_token: refreshedTokens.refresh_token ?? token.refreshToken, // Fall back to old refresh token
        }
      } catch (error) {
        console.log(error)
    
        return {
          ...token,
          error: "RefreshAccessTokenError",
        }
      }
}


export const authOptions : NextAuthOptions = {
    providers: [
        GoogleProvider(
            {
                clientId: process.env.GOOGLE_CLIENT_ID as string,
                clientSecret: process.env.GOOGLE_CLIENT_SECRET as string,
                authorization: {
                  params: {
                    prompt: "consent",
                    access_type: "offline",
                    response_type: "code"
                  }
                }
            }
        )
    ],
    session: { strategy: "jwt" },
    callbacks:{
        jwt: async ({token,user,account}) =>{
            if (user && account){
                token.access_token=account.access_token;
                token.id_token=account.id_token;
                console.log("auth/[....nextauth]/route - Account: "+JSON.stringify(account));
                token.expires= account.expires_at! * 1000;
                token.refresh_token= account.refresh_token;         

                console.log("auth/[....nextauth]/route - Account: "+ JSON.stringify(account));
                console.log("auth/[....nextauth]/route - Expires: "+token.expires);
                console.log("auth/[....nextauth]/route - Now: "+Date.now());
            }
            

            
            if (Date.now() < token.expires) {
                return {...token,...user}
            }

            return {...refreshGoogleToken(token),...user}
        },
        session: async ({session,token})=>{
            session.user=token;
            return session;
        }
    }
}
const authHandler = NextAuth(authOptions);

export {authHandler as GET, authHandler as POST}