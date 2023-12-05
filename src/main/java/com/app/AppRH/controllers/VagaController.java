package com.app.AppRH.controllers;

import com.app.AppRH.models.Candidato;
import com.app.AppRH.models.Vaga;
import com.app.AppRH.repository.CandidatoRepository;
import com.app.AppRH.repository.VagaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VagaController {

    @Autowired
    private VagaRepository vr;
    @Autowired
    private CandidatoRepository cr;


    @RequestMapping("/cadastrarVaga")
    public String form() {

        return "vaga/formVaga";
    }
    @RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
    public String form(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {

        if (result.hasErrors()) {
            attributes.addFlashAttribute("mensagem", "Verifique os campos...");
            return "redirect:/cadastrarVaga";
        }
        vr.save(vaga);
        attributes.addFlashAttribute("mensagem", "Vaga cadastrastrada com sucesso");
        return "redirect:/cadastrarVaga";
    }


    @RequestMapping("/vagas")
    public ModelAndView listaVagas() {
        var mv = new ModelAndView("vaga/listaVaga");
        Iterable<Vaga> vagas = vr.findAll();
        mv.addObject("vagas", vagas);
        return mv;
    }

    @RequestMapping(value = "/vaga/{codigo}", method = RequestMethod.GET)
    public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        var mv = new ModelAndView("vaga/detalhesVaga");
        mv.addObject("vaga", vaga);

        Iterable<Candidato> candidatos = cr.findByVaga(vaga);
        mv.addObject("candidatos", candidatos);
        return mv;
    }

    @RequestMapping("/deletarVaga")
    public String deletarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        vr.delete(vaga);
        return "redirect:/vagas";
    }

    @RequestMapping(value = "/vaga/{codigo}", method = RequestMethod.POST)
    public String detalhesVagasPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato, BindingResult result, RedirectAttributes attributes) {
         if (result.hasErrors()) {
             attributes.addFlashAttribute("mensagem", "Verifique os campos");
             return "redirect:/vaga/{codigo}";
         }
         if (cr.findByRg(candidato.getRg()) != null) {
             attributes.addFlashAttribute("mensagem_erro", "RG duplicado");
             return "redirect:/vaga/{codigo}";
         }

         Vaga vaga = vr.findByCodigo(codigo);
         candidato.setVaga(vaga);
         cr.save(candidato);
         attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso");
         return "redirect:/vaga/{codigo}";
    }

    @RequestMapping("/deletarCandidato")
    public String deletarCandidato(String rg) {
        Candidato candidato = cr.findByRg(rg);
        Vaga vaga = candidato.getVaga();
        String codigo = "" +  vaga.getCodigo();

        cr.delete(candidato);
        return "redirect:/vaga/" + codigo;
    }

    @RequestMapping(value = "/editarVaga", method = RequestMethod.GET)
    public ModelAndView editarVaga(long codigo) {
        Vaga vaga = vr.findByCodigo(codigo);
        var mv = new ModelAndView("vaga/updateVaga");
        mv.addObject("vaga", vaga);
        return mv;
    }

    @RequestMapping(value = "/editarVaga", method = RequestMethod.POST)
    public String updateVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
        vr.save(vaga);
        attributes.addFlashAttribute("success", "Vaga alterada com sucesso");

        long codigoLong = vaga.getCodigo();
        String codigo = "" + codigoLong;
        return "redirect:/vaga/" + codigo;
    }
}
