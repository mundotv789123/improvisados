Atenção
não me responsabiliso por danos que esse codigo possa causar ao seu sistema

esse codigo foi adaptado apenas no caso de migração de subdominios salvos no cloudflare para o painel pterodactyl
analise e verifique o codigo antes de executar

oque o codigo faz?
ele pega todos os subdominios do cloudflare api e verifica as portas dos servidores no banco de dados a tribui o meso a ele

primeiro ele pega todos os dns com srv, depois ele pega o targe dele e verifica na list que esta na CONFIG.JAVA
se ele encontrar na config ele verifica nos allocations no banco de dados, se o ip_alis for igual ao target do
SRV e a porta tambem ele atribui o mesmo para o servidor que esta usando o allocation, se o id do servidor for 0
ele ignora

algumas partes do codigo estão comentadas, essas partes deleta o dominio do cloudflare caso não encontre servidor correspondente
essa parte esta comentada pois pode acorrer erros e pra evitar problemas ele apenas ignora
caso vc queria deletar automaticamente é por sua conta em risco

recomendo verificar o codigo e adaptar se for preciso para sua situação