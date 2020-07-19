Atenção
não me responsabilizo por danos que esse codigo possa causar ao seu sistema

esse codigo foi adaptado apenas no caso de migração de subdominios salvos no cloudflare para o painel pterodactyl
verifique o codigo antes de executar

oque o codigo faz?
ele pegas os subdominios registrados no cloudflare e atribui ao pterodactyl de forma automática

primeiro ele pega todos os dns com srv, depois ele pega o targe dele e verifica na list que esta na CONFIG.JAVA
se ele encontrar na config ele verifica nos allocations no banco de dados, se o ip_alis for igual ao target do
SRV e a porta tambem ele atribui o mesmo para o servidor que esta usando o allocation, se o id do servidor for 0
ele ignora

algumas partes do codigo estão comentadas, essas partes deleta o dominio do cloudflare caso não encontre servidor correspondente
essa parte esta comentada pois pode ocorrer erros e pra evitar problemas ele apenas ignora
caso vc queria deletar automaticamente basta descomentar as partes, lembrando que uma vez executado não tem mais volta

recomendo verificar o codigo e adaptar se for preciso para sua situação
