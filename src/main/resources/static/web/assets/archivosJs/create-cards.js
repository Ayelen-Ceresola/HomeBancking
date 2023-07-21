let { createApp } = Vue

createApp({
    data() {
        return {
            cardType:"",
            cardColor:"",

            
        }
    },

    created() {

        
    },


    methods: {
        
        createCards() {
            
                axios.post("/api/clients/current/cards",`cardColor=${this.cardColor}&cardType=${this.cardType}`,
                    )
                    .then((res) => {
                        window.location.href = "/web/pages/cards.html"
                        

                    })
                    .catch(err => console.log(err))

            
                /*Swal.fire({
                    position: 'top-center',
                    icon: 'error',
                    title: 'already have a card of that type and color',
                    showConfirmButton: false,
                    timer: 2000,
                    customClass:{
                        popup: `alertCss`
                    }
                  })*/
                
            },
            
            logout(){
                axios.post("/api/logout")
                .then((res) => {
                    window.location.href= "/web/pages/index.html"
                })
                .catch(err => console.log(err))
            }

        },


    

}).mount("#app")