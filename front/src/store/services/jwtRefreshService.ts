import axios from "../../config/axios-config";
import JwtToken from "../models/jwt-token";

class JwtRefreshService{
    constructor() {}

    refresh(jwtToken: JwtToken){
        jwtToken = jwtToken || new JwtToken("", "");
        axios.post('/refresh/refresh', jwtToken)
        .then(function(response){

            localStorage.setItem('accessToken', response.data.data.accessToken);
            localStorage.setItem('refreshToken', response.data.data.refreshToken);
            // vue-router에서 useRouter를 import 후 화면이동하기.
            //router.push({name: "home"});
        })
        .catch(function(error){

        });
    }
}

export default JwtRefreshService