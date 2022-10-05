import axios from "../../config/axios-config";
import JwtToken from "../models/jwt-token";

class JwtRefreshService{
    constructor() {}

    refresh(jwtToken: JwtToken){
        console.log('JwtRefreshService 입니다.')
        console.log(jwtToken)

        axios.post('/refresh/refresh', jwtToken)
        .then(function(response){

            console.log(response);
            // vue-router에서 useRouter를 import 후 화면이동하기.
            //router.push({name: "home"});
        })
        .catch(function(error){

        });
    }
}

export default JwtRefreshService